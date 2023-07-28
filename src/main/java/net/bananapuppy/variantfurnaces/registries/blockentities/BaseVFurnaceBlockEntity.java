package net.bananapuppy.variantfurnaces.registries.blockentities;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.bananapuppy.variantfurnaces.registries.ModItems;
import net.bananapuppy.variantfurnaces.registries.blocks.BaseVFurnaceBlock;
import net.minecraft.SharedConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseVFurnaceBlockEntity
        extends LockableContainerBlockEntity
        implements SidedInventory,
        RecipeUnlocker,
        RecipeInputProvider {
    protected static final int INPUT_SLOT_INDEX = 0;
    protected static final int FUEL_SLOT_INDEX = 1;
    protected static final int OUTPUT_SLOT_INDEX = 2;
    public static final int BURN_TIME_PROPERTY_INDEX = 0;
    private static final int[] TOP_SLOTS = new int[]{0};
    private static final int[] BOTTOM_SLOTS = new int[]{2, 1};
    private static final int[] SIDE_SLOTS = new int[]{1};
    public static final int FUEL_TIME_PROPERTY_INDEX = 1;
    public static final int COOK_TIME_PROPERTY_INDEX = 2;
    public static final int COOK_TIME_TOTAL_PROPERTY_INDEX = 3;
    public static final int PROPERTY_COUNT = 4;
    public static final int DEFAULT_COOK_TIME = 200;
    public static final int inventorySize = 7;
    protected DefaultedList<ItemStack> inventory = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
    int burnTime;
    int fuelTime;
    int cookTime;
    int cookTimeTotal;

    public float cookTimeTotalSeconds = 10;

    public boolean fuelAugment = false;
    public boolean isFuelAugmented(){
        return this.fuelAugment;
    }
    public void setFuelAugmented(Boolean bool){
        this.fuelAugment = bool;
    }
    public boolean speedAugment = false;
    public boolean isSpeedAugmented(){
        return this.speedAugment;
    }
    public void setSpeedAugmented(Boolean bool){
        this.speedAugment = bool;
    }
    public boolean blastingAugment = false;
    public boolean isBlastingAugmented(){
        return this.blastingAugment;
    }
    public void setBlastingAugmented(Boolean bool){
        this.blastingAugment = bool;
    }
    public boolean smokeAugment = false;
    public boolean isSmokeAugmented(){
        return this.smokeAugment;
    }
    public void setSmokeAugmented(Boolean bool){
        this.smokeAugment = bool;
    }

    protected final PropertyDelegate propertyDelegate = new PropertyDelegate(){

        @SuppressWarnings("EnhancedSwitchMigration")
        @Override
        public int get(int index) {
            switch (index) {
                case 0: {
                    return BaseVFurnaceBlockEntity.this.burnTime;
                }
                case 1: {
                    return BaseVFurnaceBlockEntity.this.fuelTime;
                }
                case 2: {
                    return BaseVFurnaceBlockEntity.this.cookTime;
                }
                case 3: {
                    return BaseVFurnaceBlockEntity.this.cookTimeTotal;
                }
            }
            return 0;
        }

        @SuppressWarnings("EnhancedSwitchMigration")
        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0: {
                    BaseVFurnaceBlockEntity.this.burnTime = value;
                    break;
                }
                case 1: {
                    BaseVFurnaceBlockEntity.this.fuelTime = value;
                    break;
                }
                case 2: {
                    BaseVFurnaceBlockEntity.this.cookTime = value;
                    break;
                }
                case 3: {
                    BaseVFurnaceBlockEntity.this.cookTimeTotal = value;
                    break;
                }
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };
    private final Object2IntOpenHashMap<Identifier> recipesUsed = new Object2IntOpenHashMap<>();
    public final RecipeManager.MatchGetter<Inventory, ? extends AbstractCookingRecipe> matchGetter;

    protected BaseVFurnaceBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, RecipeType<? extends AbstractCookingRecipe> recipeType) {
        super(blockEntityType, pos, state);
        this.matchGetter = RecipeManager.createCachedMatchGetter(recipeType);
    }

    public static Map<Item, Integer> createFuelTimeMap() {
        return AbstractFurnaceBlockEntity.createFuelTimeMap();
    }

    /**
     * {@return whether the provided {@code item} is in the {@link
     * net.minecraft.registry.tag.ItemTags#NON_FLAMMABLE_WOOD non_flammable_wood} tag}
     */
    private static boolean isNonFlammableWood(Item item) {
        //noinspection deprecation
        return item.getRegistryEntry().isIn(ItemTags.NON_FLAMMABLE_WOOD);
    }

    @SuppressWarnings("unused")
    private static void addFuel(Map<Item, Integer> fuelTimes, TagKey<Item> tag, int fuelTime) {
        for (RegistryEntry<Item> registryEntry : Registries.ITEM.iterateEntries(tag)) {
            if (BaseVFurnaceBlockEntity.isNonFlammableWood(registryEntry.value())) continue;
            fuelTimes.put(registryEntry.value(), fuelTime);
        }
    }

    @SuppressWarnings("unused")
    private static void addFuel(Map<Item, Integer> fuelTimes, ItemConvertible item, int fuelTime) {
        Item item2 = item.asItem();
        if (BaseVFurnaceBlockEntity.isNonFlammableWood(item2)) {
            if (SharedConstants.isDevelopment) {
                throw Util.throwOrPause(new IllegalStateException("A developer tried to explicitly make fire resistant item " + item2.getName(null).getString() + " a furnace fuel. That will not work!"));
            }
            return;
        }
        fuelTimes.put(item2, fuelTime);
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory);
        this.burnTime = nbt.getShort("BurnTime");
        this.cookTime = nbt.getShort("CookTime");
        this.cookTimeTotal = nbt.getShort("CookTimeTotal");
        this.fuelTime = this.getFuelTime(this.inventory.get(1));
        NbtCompound nbtCompound = nbt.getCompound("RecipesUsed");
        for (String string : nbtCompound.getKeys()) {
            this.recipesUsed.put(new Identifier(string), nbtCompound.getInt(string));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putShort("BurnTime", (short)this.burnTime);
        nbt.putShort("CookTime", (short)this.cookTime);
        nbt.putShort("CookTimeTotal", (short)this.cookTimeTotal);
        Inventories.writeNbt(nbt, this.inventory);
        NbtCompound nbtCompound = new NbtCompound();
        this.recipesUsed.forEach((identifier, count) -> nbtCompound.putInt(identifier.toString(), count));
        nbt.put("RecipesUsed", nbtCompound);
    }

    public static void tick(World world, BlockPos pos, BlockState state, BaseVFurnaceBlockEntity blockEntity) {
        boolean bl4;
        boolean bl = blockEntity.isBurning();
        boolean dirty = false;
        if (blockEntity.isBurning()) {
            --blockEntity.burnTime;
        }
        ItemStack itemStack = blockEntity.inventory.get(1);
        boolean bl3 = !blockEntity.inventory.get(BURN_TIME_PROPERTY_INDEX).isEmpty();
        bl4 = !itemStack.isEmpty();
        if (blockEntity.isBurning() || bl4 && bl3) {
            @SuppressWarnings("rawtypes")
            Recipe recipe = bl3 ? blockEntity.matchGetter.getFirstMatch(blockEntity, world).orElse(null) : null;
            int i = blockEntity.getMaxCountPerStack();
            if (!blockEntity.isBurning() && BaseVFurnaceBlockEntity.canAcceptRecipeOutput(world.getRegistryManager(), recipe, blockEntity.inventory, i)) {
                blockEntity.fuelTime = blockEntity.burnTime = blockEntity.getFuelTime(itemStack);
                if (blockEntity.isBurning()) {
                    dirty = true;
                    if (bl4) {
                        Item item = itemStack.getItem();
                        itemStack.decrement(1);
                        if (itemStack.isEmpty()) {
                            Item item2 = item.getRecipeRemainder();
                            blockEntity.inventory.set(1, item2 == null ? ItemStack.EMPTY : new ItemStack(item2));
                        }
                    }
                }
            }
            if (blockEntity.isBurning() && BaseVFurnaceBlockEntity.canAcceptRecipeOutput(world.getRegistryManager(), recipe, blockEntity.inventory, i)) {
                ++blockEntity.cookTime;
                if (blockEntity.cookTime == blockEntity.cookTimeTotal) {
                    blockEntity.cookTime = 0;
                    blockEntity.cookTimeTotal = BaseVFurnaceBlockEntity.getCookTime(world, blockEntity);
                    if (BaseVFurnaceBlockEntity.craftRecipe(world.getRegistryManager(), recipe, blockEntity.inventory, i)) {
                        blockEntity.setLastRecipe(recipe);
                    }
                    dirty = true;
                }
            } else {
                blockEntity.cookTime = 0;
            }
        } else if (!blockEntity.isBurning() && blockEntity.cookTime > 0) {
            blockEntity.cookTime = MathHelper.clamp(blockEntity.cookTime - 2, 0, blockEntity.cookTimeTotal);
        }
        if (bl != blockEntity.isBurning()) {
            dirty = true;
            state = state.with(BaseVFurnaceBlock.LIT, blockEntity.isBurning());
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
        }

        //Augment Checker
        List<Item> augmentSlots = List.of(
                blockEntity.inventory.get(3).getItem(),
                blockEntity.inventory.get(4).getItem(),
                blockEntity.inventory.get(5).getItem(),
                blockEntity.inventory.get(6).getItem()
        );
        Boolean[] augments = {false, false, false , false};
        for(Item item : augmentSlots){ //TODO:
            if(item.equals(ModItems.FUEL_AUGMENT)){
                augments[0] = true;
                blockEntity.setFuelAugmented(true);
            }
            if(item.equals(ModItems.SPEED_AUGMENT)){
                augments[1] = true;
                blockEntity.setSpeedAugmented(true);
            }
            if(item.equals(ModItems.BLASTING_AUGMENT)){
                augments[2] = true;
                blockEntity.setBlastingAugmented(true);
            }
            if(item.equals(ModItems.SMOKE_AUGMENT)){
                augments[3] = true;
                blockEntity.setSmokeAugmented(true);
            }
        }
        if(!augments[0]){blockEntity.setFuelAugmented(false);}
        if(!augments[1]){blockEntity.setSpeedAugmented(false);}
        if(!augments[2]){blockEntity.setBlastingAugmented(false);}
        if(!augments[3]){blockEntity.setSmokeAugmented(false);}

        //Block Augment State Updater
        dirty = augmentBlockEntity(state, world, pos, blockEntity);

        if (dirty) {
            BaseVFurnaceBlockEntity.markDirty(world, pos, state);
        }
    }

    private static Boolean augmentBlockEntity(BlockState state, World world, BlockPos pos, BaseVFurnaceBlockEntity blockEntity){
        boolean dirty = false;
        if(blockEntity.isFuelAugmented() != state.get(BaseVFurnaceBlock.FUEL_AUGMENT)){
            dirty = true;
            state = state.with(BaseVFurnaceBlock.FUEL_AUGMENT, !blockEntity.isFuelAugmented());
        }
        if(blockEntity.isSpeedAugmented() != state.get(BaseVFurnaceBlock.SPEED_AUGMENT)){
            dirty = true;
            state = state.with(BaseVFurnaceBlock.SPEED_AUGMENT, !blockEntity.isSpeedAugmented());
        }
        if(blockEntity.isBlastingAugmented() != state.get(BaseVFurnaceBlock.BLASTING_AUGMENT)){
            dirty = true;
            state = state.with(BaseVFurnaceBlock.BLASTING_AUGMENT, !blockEntity.isBlastingAugmented());
        }
        if(blockEntity.isSmokeAugmented() != state.get(BaseVFurnaceBlock.SMOKE_AUGMENT)){
            dirty = true;
            state = state.with(BaseVFurnaceBlock.SMOKE_AUGMENT, !blockEntity.isSmokeAugmented());
        }

        if(dirty){
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
        }
        return dirty;
    }

    private static boolean canAcceptRecipeOutput(DynamicRegistryManager registryManager, @Nullable Recipe<?> recipe, DefaultedList<ItemStack> slots, int count) {
        if (slots.get(INPUT_SLOT_INDEX).isEmpty() || recipe == null) {
            return false;
        }
        ItemStack itemStack = recipe.getOutput(registryManager);
        if (itemStack.isEmpty()) {
            return false;
        }
        ItemStack itemStack2 = slots.get(OUTPUT_SLOT_INDEX);
        if (itemStack2.isEmpty()) {
            return true;
        }
        if (!ItemStack.areItemsEqual(itemStack2, itemStack)) {
            return false;
        }
        if (itemStack2.getCount() < count && itemStack2.getCount() < itemStack2.getMaxCount()) {
            return true;
        }
        return itemStack2.getCount() < itemStack.getMaxCount();
    }

    private static boolean craftRecipe(DynamicRegistryManager registryManager, @Nullable Recipe<?> recipe, DefaultedList<ItemStack> slots, int count) {
        if (recipe == null || !BaseVFurnaceBlockEntity.canAcceptRecipeOutput(registryManager, recipe, slots, count)) {
            return false;
        }
        ItemStack itemStack = slots.get(INPUT_SLOT_INDEX);
        ItemStack itemStack2 = recipe.getOutput(registryManager);
        ItemStack itemStack3 = slots.get(COOK_TIME_PROPERTY_INDEX);
        if (itemStack3.isEmpty()) {
            slots.set(2, itemStack2.copy());
        } else if (itemStack3.isOf(itemStack2.getItem())) {
            itemStack3.increment(1);
        }
        if (itemStack.isOf(Blocks.WET_SPONGE.asItem()) && !slots.get(FUEL_TIME_PROPERTY_INDEX).isEmpty() && slots.get(FUEL_SLOT_INDEX).isOf(Items.BUCKET)) {
            slots.set(1, new ItemStack(Items.WATER_BUCKET));
        }
        itemStack.decrement(1);
        return true;
    }

    protected int getFuelTime(ItemStack fuel) {
        if (fuel.isEmpty()) { return 0; }
        Item item = fuel.getItem();
        int fuelTime = createFuelTimeMap().getOrDefault(item, 0);
        if(isFuelAugmented()){
            fuelTime = fuelTime * 2;
        }
        if(isSpeedAugmented()){
            fuelTime = fuelTime / 2;
        }

        return fuelTime;
    }

    private static int getCookTime(World world, BaseVFurnaceBlockEntity furnace) {
        int cookTime = (int)((float)(furnace.matchGetter.getFirstMatch(furnace, world).map(AbstractCookingRecipe::getCookTime).orElse(DEFAULT_COOK_TIME) / 10) * furnace.cookTimeTotalSeconds);
        if(furnace.isFuelAugmented()){
            cookTime = cookTime * 4 / 3;
        }
        if(furnace.isSpeedAugmented()){
            cookTime = cookTime / 2;
        }
        if(furnace.isBlastingAugmented() | furnace.isSmokeAugmented()){
            cookTime = cookTime / 2;
        }
        return cookTime;
    }

    public static boolean canUseAsFuel(ItemStack stack) {
        return createFuelTimeMap().containsKey(stack.getItem());
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.DOWN) {
            return BOTTOM_SLOTS;
        }
        if (side == Direction.UP) {
            return TOP_SLOTS;
        }
        return SIDE_SLOTS;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isValid(slot, stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        if (dir == Direction.DOWN && slot == 1) {
            return stack.isOf(Items.WATER_BUCKET) || stack.isOf(Items.BUCKET);
        }
        return true;
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.inventory) {
            if (itemStack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        ItemStack itemStack = this.inventory.get(slot);
        boolean bl = !stack.isEmpty() && ItemStack.canCombine(itemStack, stack);
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }
        if (slot == 0 && !bl) {
            this.cookTimeTotal = BaseVFurnaceBlockEntity.getCookTime(this.world, this);
            this.cookTime = 0;
            this.markDirty();
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return Inventory.canPlayerUse(this, player);
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if (slot == 2) {
            return false;
        }
        if (slot == 1) {
            ItemStack itemStack = this.inventory.get(FUEL_SLOT_INDEX);
            return BaseVFurnaceBlockEntity.canUseAsFuel(stack) || stack.isOf(Items.BUCKET) && !itemStack.isOf(Items.BUCKET);
        }
        return true;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    @Override
    public void setLastRecipe(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            Identifier identifier = recipe.getId();
            this.recipesUsed.addTo(identifier, 1);
        }
    }

    @Override
    @Nullable
    public Recipe<?> getLastRecipe() {
        return null;
    }

    @Override
    public void unlockLastRecipe(PlayerEntity player, List<ItemStack> ingredients) {
    }

    public void dropExperienceForRecipesUsed(ServerPlayerEntity player) {
        List<Recipe<?>> list = this.getRecipesUsedAndDropExperience(player.getServerWorld(), player.getPos());
        player.unlockRecipes(list);
        for (Recipe<?> recipe : list) {
            if (recipe == null) continue;
            player.onRecipeCrafted(recipe, this.inventory);
        }
        this.recipesUsed.clear();
    }

    @SuppressWarnings("rawtypes")
    public List<Recipe<?>> getRecipesUsedAndDropExperience(ServerWorld world, Vec3d pos) {
        ArrayList<Recipe<?>> list = Lists.newArrayList();
        for (Object2IntMap.Entry entry : this.recipesUsed.object2IntEntrySet()) {
            world.getRecipeManager().get((Identifier)entry.getKey()).ifPresent(recipe -> {
                list.add(recipe);
                BaseVFurnaceBlockEntity.dropExperience(world, pos, entry.getIntValue(), ((AbstractCookingRecipe)recipe).getExperience());
            });
        }
        return list;
    }

    private static void dropExperience(ServerWorld world, Vec3d pos, int multiplier, float experience) {
        int i = MathHelper.floor((float)multiplier * experience);
        float f = MathHelper.fractionalPart((float)multiplier * experience);
        if (f != 0.0f && Math.random() < (double)f) {
            ++i;
        }
        ExperienceOrbEntity.spawn(world, pos, i);
    }

    @Override
    public void provideRecipeInputs(RecipeMatcher finder) {
        for (ItemStack itemStack : this.inventory) {
            finder.addInput(itemStack);
        }
    }
}

