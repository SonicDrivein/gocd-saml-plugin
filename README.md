# GoCD SAML Auth Plugin

This plugin allows you to use SAML as you identity provider for GoCD. It has been tested with Centrify as the provider.

The documentation for authorization plugins is hosted [here](https://plugin-api.gocd.io/current/authorization/)

## Attribution & History

This plugin would not be possible without the partnership of ThoughtWorks with SONIC.  ThoughtWorks' fabulous Delivery Infrastructure team built this plugin, primarily by the contributions of:
* Duda Dornelles (ddornell-at-thoughtworks.com)
* Christine Rohacz (crohacz-at-thoughtworks.com)

## Getting started

* Download the jar and install it in gocd's plugin folder (`<go server home>/plugins/external`)
* Get the SAML Provider metadata file and place it somewhere inside the gocd server, e.g.: `/etc/go/saml_metadata.xml`
* Grab the login url from your provider - you'll need it for the next step. E.g.: `https://provider/app-id/login`
* Configure the security section on cruise-config.xml. e.g.:
```
<security>
  <authConfigs>
    <authConfig id="centrify" pluginId="cd.go.auth.saml">
      <property>
        <key>AuthorizationUrlField</key>
        <value>https://provider/app-id/login</value>
      </property>
      <property>
        <key>SamlMetadataPath</key>
        <value>/etc/go/saml_metadata.xml</value>
      </property>
    </authConfig>
  </authConfigs>
</security>
```

This will set you up with AuthC. To add authorization, you'll need two things:

* Make sure your provider is sending an `Attribute` with `Name=Groups` as in the example below

```
<AttributeStatement>
   <Attribute Name="Groups" NameFormat="urn:oasis:names:tc:SAML:2.0:attrname-format:basic">
       <AttributeValue>group_a</AttributeValue>
       <AttributeValue>group_b</AttributeValue>
   </Attribute>
</AttributeStatement>
```

* In `cruise-config.xml`, add plugin roles as below (notice how the `authConfigId` has to match the one in your `<authConfig>` entry):
```
<security>
    <roles>
      <pluginRole name="group_a" authConfigId="centrify" />
      <pluginRole name="group_b" authConfigId="centrify" />
    </roles>
</security>
```

## Development

To build the jar, run `./gradlew clean test assemble`

## License

```plain
Copyright 2017 Sonic America's Drive In (Sonic Corporation)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
