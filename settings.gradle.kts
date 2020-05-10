rootProject.name = "gradle-oss-licenses-plugin-ext"

include(":demo", ":plugin")
project(":plugin").projectDir = file("buildSrc")
