# PlantUML Service

Use something like the following as embedded UML diagram in your documentation
in Github flavoured Markdown:

    ![MyModel](http://localhost:8080/plantuml/png?source=
    @startuml
    Hans -> John : Hallo
    John -> Carla : Hi
    Carla -> Hans : Ola!
    @enduml
    )

You can add the typical `@startuml` and `@enduml` but it's not needed as that's
the default. Sample:

    ![MyModel](http://localhost:8080/plantuml/png?source=
    Hans -> John : Hallo
    John -> Carla : Hi
    Carla -> Hans : Ola!
    )


Use `svg` or `atxt` as alternatives to get graphs as SVG or as ASCII.

**Don't put empty lines** inside the body as that closes the Markdown image tag
and be sure to **escape round brackets** for the same reason. Sample:

    ![MyModel](http://localhost:8080/plantuml/png?source=
    Hans -> John : "Hallo" \(Deutsch\)
    John -> Carla : "Hi" \(English\)
    Carla -> Hans : "Ola!" \(Portuguese\)
    )

Getting fanzy with one of the non-UML diagrams supported by PlantUML? Use Salt
for user interface design:

	![MyModel](http://localhost:8080/plantuml/png?source=
	@startsalt
	{
	  Login    | "MyName   "
	  Password | "****     "
	  [Cancel] | [  OK   ]
	}
	@endsalt
	)

Learn **more about fantastic PlantUML** at http://plantuml.com and don't be
fooled by the old-school website: PlantUML is rock and roll!

We could add some caching, there could be some more documentation and we
definitely have to reduce the docker image size, but the current version is
good enough to give it a try. Enjoy.

## Docker

Deployment as a docker image is done like this:

    docker build -t plantuml-service:rev-`git rev-parse --short HEAD` -t plantuml-service:latest .
    docker run -it -p 8080:8080 --rm plantuml-service:latest
