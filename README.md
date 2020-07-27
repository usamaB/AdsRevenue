# AdsRevenue Guide
## Decision 
Remove impressions with null or country_code set as Empty. from empty the analyst or Data Scientist can't tell which country it is. 
If want to enable it's just a check.

## How to Run
### parameters 
1) -i or --impressions-file-path absolute path of impressions.json
2) -c or --clicks-file-path absolute path of clicks.json
3) -o or --output-file-path optional. absolute path of outputfile. Default is metrics.json
 
to run is sbt-shell. goto project path and or can also run from sbt-shell 
```sbtshell
sbt run -i absolute_path_of_impressions.json -c absolute_path_of_clicks.json
```

for tests
```sbtshell
sbt test
```

to generate jar
```sbtshell
assembly
```
 
to run the application
```bash
scala AdsRevenue-assembly-0.1-SNAPSHOT.jar -i impressions.json -c clicks.json
```

## Result
If you provide **-o or --output-file-path** parameter the output will be generated at that path, else it'll be generated at the same path you run the command/application.


# TBD
Integration tests
