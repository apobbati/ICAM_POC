useracct-war:
  file.symlink:
    - name: /var/lib/tomcat7/webapps/ROOT.war
    - target: /srv/repo/UserAcctMgmt/target/UserAcctMgmt.war
    - force: True
    - require:
      - pkg: tomcat7