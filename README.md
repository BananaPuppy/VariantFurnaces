## Project Build Guide
Open build.gradle with IntelliJ

Edit
- gradle.properties
  - mod_version 
  - maven_group (net.yourname)
  - archives_base_name (mod id)
  - https://fabricmc.net/develop/
    - minecraft_version 
    - yarn_mappings 
    - loader_version 
    - fabric_version 
- /src/*
  - /java/maven_group/mod_id
  - resources 
    - /assets/mod_id
    - /data/mod_id
	- mod_id.mixins.json
	- fabric.mod.json
		- id, entrypoints


	./gradlew --stop

Gradle >Tasks >fabric >genSources

### Github Publish
Alt+` >Enable Version Control Integration >Git<br>
Git >Github >Share Project On Github
LICENSE

### Build
Gradle >Tasks >build >build
- The .jar can be found in /build/libs/

## Modrinth Release
Complete TODO: comments<br>
Clean up warnings<br>
fabric.mod.json
- Name, Description(Summary), Authors, Icon, Depends

Build & Run & Stress Test<br>

**Modrinth Page**
- Icon, Name, Summary, Tags
- Client Required? Server Required?
- License, link to LICENSE on repo
- Links(Issue Tracker, Repo, Etc)
- Members, Gallery
- Upload Version
- Description from README_TEMPLATE.md
- commit new README to github
- Submit for review