/etc/sysctl.conf:
  file:
    - managed
    - source: salt://requirements/sysctl.conf

/etc/security/limits.conf:
  file:
    - managed
    - source: salt://requirements/limits.conf

set-limits:
  cmd.wait:
    - name: 'sysctl -p'
    - user: root
    - watch:
      - file: /etc/security/limits.conf
      - file: /etc/sysctl.conf
