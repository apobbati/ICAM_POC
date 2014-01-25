/usr/share/tomcat7:
  file:
    - directory
    - user: tomcat7
    - group: tomcat7
    - mode: 755
    - recurse:
      - mode

openam-dist:
  archive:
    - extracted
    - name: /opt/openam
    - source: http://download.forgerock.org/downloads/openam/OpenAM-12.0.0-SNAPSHOT_nightly_20140123.zip
    - archive_format: zip
    - source_hash: md5=9e5b6bfabcd6ff6d61c141720d95b274
    - require:
      - pkg: tomcat7

openam.war:
  file:
    - managed
    - name: /var/lib/tomcat7/webapps/openam.war
    - source: file:///opt/openam/openam/OpenAM-12.0.0-SNAPSHOT.war
    - source_hash: md5=819da40f145b440f747cabfbf35114df
    - require:
      - archive: openam-dist

openam-admin-tools:
  archive:
    - extracted
    - name: /usr/share/tomcat7/openam-admin-tools
    - source: file:///opt/openam/openam/SSOAdminTools-12.0.0-SNAPSHOT.zip
    - source_hash: md5=d3475b5e33d7e264e55ef5f4d87a3e9c
    - archive_format: zip
    - require:
      - archive: openam-dist

openam-configurator-tools:
  archive:
    - extracted
    - name: /usr/share/tomcat7/openam-configurator-tools
    - source: file:///opt/openam/openam/SSOConfiguratorTools-12.0.0-SNAPSHOT.zip
    - source_hash: md5=3562c4d82ee44f5d816185d9bd30dd27
    - archive_format: zip
    - require:
      - archive: openam-dist

openam-configuration-setup:
  cmd.run:
    - name: 'java -jar openam-configurator-tool-12.0.0-SNAPSHOT.jar -f /srv/salt/openam12/config/configuration.properties'
    - cwd: /usr/share/tomcat7/openam-configurator-tools
    - user: tomcat7
    - require:
      - archive: openam-configurator-tools

openam-admin-logs-dir:
  file.directory:
    - name: /usr/share/tomcat7/logs/openam
    - user: tomcat7
    - group: tomcat7
    - makedirs: True

openam-admin-pwd-file:
  file.managed:
    - name: /usr/share/tomcat7/amadmin.pwd
    - source: salt://openam12/config/amadmin.pwd
    - user: tomcat7
    - group: tomcat7
    - mode: 400

openam-admintools-setup:
  cmd.run:
    - name: './setup -p /usr/share/tomcat7/openam -d /usr/share/tomcat7/logs/openam/debug -l /usr/share/tomcat7/logs/openam/log'
    - cwd: /usr/share/tomcat7/openam-admin-tools
    - user: tomcat7
    - require:
      - archive: openam-admin-tools
      - file: openam-admin-logs-dir
      - file: openam-admin-pwd-file
      - cmd: openam-configuration-setup

openam-provision-tools:
  cmd.script:
    - name: salt://openam12/provision-config.sh
    - user: tomcat7
    - cwd: /usr/share/tomcat7/openam-admin-tools/openam/bin
    - require:
      - cmd: openam-admintools-setup
