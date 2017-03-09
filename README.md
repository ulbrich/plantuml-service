# PlantUML Service

Use something like the following as embedded UML diagram in your documentation
in Github flavoured Markdown:

    ![MyModel](http://127.0.0.1:8080/plantuml/png?source=
    Frank -> Ben : Hallo
    Ben -> Carla : Hi
    Carla -> Frank : Ola!
    )

You can add the typical `@startuml` and `@enduml` but it's not needed. Sample:

    ![MyModel](http://127.0.0.1:8080/plantuml/png?source=
    @startuml
    Frank -> Ben : Hallo
    Ben -> Carla : Hi
    Carla -> Frank : Ola!
    @enduml
    )


Use `svg` or `atxt` as alternatives to get graphs as SVG or as ASCII.

Learn more about fantastic PlantUML here: http://plantuml.com

We could add some caching and there could be some more documentation but its
good enough to give it a try. Enjoy.

## Docker

Deployment as a docker image is done like this:

    docker build -t plantuml-service:latest .
    docker run -it -p 8080:8080 --rm plantuml-service:latest
