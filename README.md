# The Cool Mini HTTP Web Server
HTTP/1.1 Server written in java using sockets for COSC 350

# Requirements
JDK 25

# Compile and Run
From Project root
```bash
mkdir -p out
javac -d out -sourcepath src/main/java src/main/java/MyWebServer.java
java -cp out MyWebServer 
```


# For easy testing: 

# GET
```bash
curl -i http://localhost:8888/test.txt or /
```

# Head
```bash
curl -I http://localhost:8888/test.txt or /
```

# 404 Not found
```bash
curl -i http://localhost:8888/tombombadil.txt
```

# 403 Forbidden
```bash
curl -i --path-as-is http://localhost:8888/../../password
```

# 501 Not Implemented
```bash
curl -i -X POST http://localhost:8888/
```

# Conditional GET
```bash
curl -i \
  -H "If-Modified-Since: <copy the Last-Modified value here>" \
  http://localhost:8888/
  ```

# keep alive
```bash
curl -v http://localhost:8888/test.txt http://localhost:8888/
```