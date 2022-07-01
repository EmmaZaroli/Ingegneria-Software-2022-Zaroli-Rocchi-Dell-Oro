# Eriantys

## Team info

Number: GC10

### Members

- [Emma Zaroli](https://github.com/emmazaroli)
- [Marco Rocchi](https://github.com/marcorocchi)
- [Riccardo Dell'Oro](https://github.com/riccardodelloro)

## Development status

| Feature        | Status             |
| -------------- | ------------------ |
| Basic rules    | :white_check_mark: |
| Complete rules | :white_check_mark: |
| CLI            | :white_check_mark: |
| GUI            | :white_check_mark: |
| Socket         | :white_check_mark: |
| Multiple games | :white_check_mark: |
| Persistence    | :white_check_mark: |
| Disconnections | :white_check_mark: |

## Running the Applications

### Server

```
java -jar Server.jar --port <port>
```

### Client

```
java -jar Client.jar [--cli]
```

Omitting the argument --cli will start the GUI version of the client.  
Please, note that the cli supports Linux and MacOS only.

## Tests coverage report

For a complete report, please refer to the [full data](Deliverables/Final/Test%20report) in the deliverables folder

![](Deliverables/Final/High%20level%20coverage%20data.png)

## Design documents

Design documents are stored in the [Deliverables](Deliverables/Final) folder. JavaDoc documents are also available.

## Additional tools used

- **SonarLint** To increase code quality and identify code smells
- **SaveActions** To format and beautify our code in a standardized manner
- **Metrics reloaded** To calculate code metrics and identify hard-to-maintain code
- **GitHub Actions** To automatically run unit tests at every push
