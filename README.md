## How to fix library error when cloning our git repo

1. Check setting.json
   it ensure that is should look somethinig like this:

   ```
    {
    "java.project.sourcePaths": ["src"],
    "java.project.outputPath": "bin",
    "java.project.referencedLibraries": {
        "include": ["lib/**/*.jar"]
    }
    }

    ```


2. Check lauch.json

   
   When try to clone the repo for vscode, it try to create the new configuration for the project, here is how to fix it:

   original lanch.json:

   ```
    {
        "version": "0.2.0",
        "configurations": [
            {
                "type": "java",
                "name": "Main",
                "request": "launch",
                "mainClass": "Main",
                "projectName": "cpu-scheduling_705f341a",
                "vmArgs": "--module-path lib/libs --add-modules javafx.controls,javafx.fxml,javafx.graphics"
            }
        ]
    }
    ```

    after clone it should look something like this:
    
    ```
        {
        "version": "0.2.0",
        "configurations": [
            {
                "type": "java",
                "name": "Main",
                "request": "launch",
                "mainClass": "Main",
                "projectName": "cpu-scheduling_409287b", <-- this can be change, base on your machine
                "vmArgs": "--module-path lib/libs --add-modules javafx.controls,javafx.fxml,javafx.graphics"
            },

            {
                "type": "java",
                "name": "Main",
                "request": "launch",
                "mainClass": "Main",
                "projectName": "cpu-scheduling_705f341a",
                "vmArgs": "--module-path lib/libs --add-modules javafx.controls,javafx.fxml,javafx.graphics"
            }
        ]
    }

    ```

    - first copy the project name for the first configuration ( in this case: cpu-scheduling_409287b) then paste to the project below the first configuration
    - second, command out the first configuration and save it
    - also check the java jdk version, by search: Java: Configure Java Runtime, it should be: 21.0.8-win32-x86_64




