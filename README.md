# Kotlin2PlantUML

This library allows you to obtain a plantuml diagram directly from your Kotlin (and Java)
code base. You can either obtain a simple class representation or a class hierarchy.

## Usage

### Command line

---------------------------------------------------
The arguments tha will be passed to main function are:

 ```bash
fullyQualifiedClass [...options]
 ```

---------------------------------------------------
 #### Options:

---------------------------------------------------

* **-o, --output**: output file
* **-r, --recurse**: explore class hierarchy recursively
* **-hf, --hide-fields**: hide fields
* **-hm, --hide-methods**: hide methods
* **-hr, --hide-relationships**: hide relationships
##### Visibility:
The order is defined by the `KVisibility` enum:
{ 0: Public, 1: Protected, 2: Internal, 3: Private }
* **-fv, --field-visibility**: max field visibility
* **-mv, --method-visibility**: max method visibility

Based on the passed arguments a `Configuration` will be created
to customize the output.

### Example
```kotlin
ClassDiagram(
    MyRootClass::class, 
    configuration = Configuration(recurse = true)
).plantUml()
```

