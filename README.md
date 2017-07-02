# spark-streaming-monitoring-with-lightning
Have your custom graphs to monitor your spark application real-time using lightning-viz

# spark-streaming-monitoring-with-lightning
Real-time Spark Stream Monitoring over SocketStream. Using Apache Spark and Lightning Graph server. ApacheSpark 2.x streaming application with Dataset’s is not supporting ```streaming``` tab now. This project is to have a realtime monitoring graph where you can have graphs of any custom param that you need. In this project, am plotting a graph of ```processing time taken per batch``` and ```number of records per batch``` which get updated as and when a new batch is received

Following picture depicts side-by-side view of spark-metrics page and its corresponding BatchProcessingTime and NumRecordsPerBatch plotted live
![Running example](https://user-images.githubusercontent.com/22542670/27770239-5e636fa6-5f58-11e7-8b72-28470de103dd.png)

## 1. Building

This project is using [mvn](#mvn), [scala](#scala) and [java](#java).

```sh
$ mvn clean install
```

## 2. Pre-execution

### 2.1. Lightning Graph Server

First of all, the application depends on Lightning Graph Server.

The default server is http://public.lightning-viz.org, but you can [![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/lightning-viz/lightning/tree/v1.2.1) or [Install](#lightning) on your machine

## 3. Command-line

It's possible to execute the spark job by command-line, without changing configuration files.

First of all, there are 2 ways to execute the application:

1. standalone jar
```sh
$ scala -extdirs “$SPARK_HOME/lib" <path-to-spark-streaming-monitoring-with-lightning.jar> --master <master>
```

2. spark-submit
```sh
$ spark-submit --master <master> <path-to-spark-streaming-monitoring-with-lightning.jar>
```

Without master parameter, the default is local[2].

## 3.1. Help command

```sh
$ <command> --help
OR
$ <command> -h
```

## 4. Configuration
Just only spark job needs a configuration. It's also configurable by command-line.
You can see de command options running:

### 4.1. Twitter access tokens

```sh
$ <command> --consumerKey xxxxxxxxxxxxxxxxxxxxxxxxx \
--consumerSecret xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx \
--accessToken xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx \
--accessTokenSecret xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

### 4.2. Servers

```sh
$ <command> --lightning http://localhost:3000 \
--twtweb http://localhost:8888
```

### 4.3. File configuration
Default values for all the options available from command-line are also present in configuration file. You can directly tweak the file instead of submitting it every time from run/submit command as shown below:
**/src/main/resources/dev/application.properties**
```ini
...
sparkMaster=local[2]
socketStreamPort=9999
socketStreamHost=localhost
appName=sparkmonitoring-with-lightning
batchInterval=5
lightningServerUrl=http://localhost:3000
...
```
