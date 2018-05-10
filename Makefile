install: build
	(pkill -f "Go Server") || true
	cp build/libs/gocd-saml-plugin-0.0.0.jar "${HOME}/Library/Application Support/Go Server/plugins/external"
	sleep 4
	open "/Applications/Go Server.app"/

build:
	./gradlew clean assemble -x test
	
.PHONY: install build
