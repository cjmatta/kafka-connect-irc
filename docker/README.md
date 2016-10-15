#### Testing with Docker
After running `mvn clean package` you can create a small Confluent Platform environment to test the connector in using Docker, and listen to the incoming messages on the console using `listen_to_wikipedia_topic.sh`.

##### Start the environment
```
$ docker-compose up
```

##### Submit the test config
This will submit a connector that listens to the English language changes channel on irc.wikimedia.org
```
$ ./submit_wikipedia_config.sh
```