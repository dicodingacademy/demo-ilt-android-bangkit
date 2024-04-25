[1] We will check the results of the migration through Logcat.
```
it.forEach(::println)
```

[2] The primary prerequisite for automated migration is enabling database schema export.
```
    exportSchema = true
```

[3] To define the location of the schema to be exported, add the javaCompileOptions block to build.gradle(Module: app):
```
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
```
[4] Once the exportSchema process has been completed successfully, the next step is to simulate a database change. Try adding a new column to the Student table with a new field.
```
    @ColumnInfo(defaultValue = "false")
    val graduate: Boolean? = false
```

[5] Upon launching the application, you will encounter a force close accompanied by an error message as shown in Logcat.
```
java.lang.IllegalStateException: Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number.
```

[6] To resolve the aforementioned error, upgrade the database version to 2 in StudentDatabase.
```
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
    ],
```

[7] The next scenario involves renaming a column. For instance, if the newly added field doesn't align with naming conventions, let's rename it from graduate to isGraduate in the StudentEntity class.
```
    val isGraduate: Boolean? = false
```
[8] The next step involves adding AutoMigration as before. However, there's a slight difference here. When renaming a field, the system doesn't automatically detect the change. Instead, it treats the new name as a new field and removes the old one. To address this, we need to add a specification.
```
version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = StudentDatabase.MyAutoMigration::class),
    ],
```

```
@RenameColumn(tableName = "Student", fromColumnName = "graduate", toColumnName = "isGraduate")
class MyAutoMigration : AutoMigrationSpec
```

