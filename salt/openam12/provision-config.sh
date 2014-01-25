#!/bin/bash

PWD_FILE=/usr/share/tomcat7/amadmin.pwd
COT=mycot
./ssoadm create-cot -u amadmin -f $PWD_FILE -e / -t $COT
./ssoadm import-entity -u amadmin -f $PWD_FILE -e / -t $COT -m /srv/salt/openam12/config/myidp-metadata.xml -x /srv/salt/openam12/config/myidp-extended-metadata.xml
./ssoadm import-entity -u amadmin -f $PWD_FILE -e / -t $COT -m /srv/salt/openam12/config/useracctmgmt-metadata.xml -x /srv/salt/openam12/config/useracctmgmt-extended-metadata.xml
./ssoadm import-entity -u amadmin -f $PWD_FILE -e / -t $COT -m /srv/salt/openam12/config/externalapp-metadata.xml -x /srv/salt/openam12/config/externalapp-extended-metadata.xml
