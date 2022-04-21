# Kotlin2PlantUML

This library allows you to obtain a plantuml class diagram directly from your Kotlin (and Java)
code base. You can either generate a simple class uml representation or the hierarchy of a root class.

## Usage

### Command line

---------------------------------------------------
One way to generate a PlantUML class diagram is by executing the [jar file](https://github.com/kelvindev15/Kotlin2PlantUML/releases/download/latest/kotlin2plantuml.jar).
Arguments can be passed with the following structure:

 ```bash
java -jar kotlin2plantuml.jar fullyQualifiedClass [...options]
 ```
At least one argument must be passed. The first argument must be a ***fully qualified***
class name, otherwise an error will be thrown.

> ***Note***: The library uses the system ClassLoader to load classes thus only classes available in the
> run time classpath will be found. Running the library via jar may require that you also set it's
> classpath.

---------------------------------------------------
 #### Options:

---------------------------------------------------
To understand which options can be passed you can run
```bash
java -jar kotlin2plantuml.jar --help
```
Here's the output of the ***help*** command:
```text
usage: java -jar kotlin2plantuml.jar full.class.name [...options]
 -fv,--field-visibility <arg>    Max. field visibility (0=Public,
                                 1=Protected, 2=Internal, 3=Private)
 -h,--help                       Display this message
 -hf,--hide-fields               Hide fields on classes
 -hm,--hide-methods              Hide methods on classes
 -hr,--hide-relationships        Hide relationships between classes
 -mv,--method-visibility <arg>   Max. method visibility (0=Public,
                                 1=Protected, 2=Internal, 3=Private)
 -o,--output <arg>               Output file path
 -p,--packages <arg>             ':' separated packages (for subclasses)
 -r,--recurse                    Visit class class hierarchy
```

Based on the passed arguments a `Configuration` will be created
to customize the output.

### Example
```bash
java -jar kotlin2plantuml.jar my.fully.qualified.Class --packages look.here:and.also.here --recurse 
```

By default, the plantUml output file will be placed in *build/reports/* named *diagram.plantuml*.
You can change this by setting the output file with the `--output` option:
```bash
java -jar kotlin2plantuml.jar my.fully.qualified.Class --output ./path/to/file.plantuml 
```

### Import into a Gradle project

Another possibility is to import the library into a gradle project.
Just add this to your build file:
```kotlin
implementation("io.github.kelvindev15:Kotlin2PlantUML:<latest-version>")
```
and you're good to go. Now you can freely use the library API. Here's an example:

```kotlin
val myPlantUML = ClassDiagram(
    MyRootClass::class,
    configuration = Configuration(recurse = true)
).plantUml()
```

