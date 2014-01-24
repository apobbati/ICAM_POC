# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

domain = "acorn.ads.example.com"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  config.hostmanager.enabled = true
  config.hostmanager.manage_host = true
  config.hostmanager.ignore_private_ip = false
  config.hostmanager.include_offline = true

  config.vm.define vm_name = "openam" do |config|
    config.vm.box = "precise64"
    config.vm.hostname = "sso." + domain
    config.vm.network :private_network, ip: "192.168.43.20"

    config.vm.provider "virtualbox" do |v|
      v.memory = 2048
    end

    config.vm.synced_folder "./",    "/srv/repo/"
    config.vm.synced_folder "salt/", "/srv/salt/"
    config.vm.synced_folder "pillars/", "/srv/pillars/"

    config.vm.provision :salt do |salt|
      salt.minion_config = "salt/minion.conf"
      salt.run_highstate = true
      salt.verbose = true
      salt.install_type = "git"
      salt.install_args = "develop"
    end
  end

  #config.vm.define vm_name = "useracctmgmt" do |config|
  #  config.vm.box = "precise64"
  #  config.vm.hostname = "useracct." + domain
  #  config.vm.network :private_network, ip: "192.168.43.21"
  #
  #  config.vm.provider "virtualbox" do |v|
  #    v.memory = 1024
  #  end
  #
  #  config.vm.synced_folder "./",    "/srv/repo/"
  #  config.vm.synced_folder "salt/", "/srv/salt/"
  #  config.vm.synced_folder "pillars/", "/srv/pillars/"
  #
  #  config.vm.provision :salt do |salt|
  #    salt.minion_config = "salt/minion.conf"
  #    salt.run_highstate = true
  #    salt.verbose = true
  #  end
  #end

  #config.vm.define vm_name = "externalapp" do |config|
  #  config.vm.box = "precise64"
  #  config.vm.hostname = "externalapp." + domain
  #  config.vm.network :private_network, ip: "192.168.43.22"
  #
  #  config.vm.provider "virtualbox" do |v|
  #    v.memory = 1024
  #  end
  #
  #  config.vm.synced_folder "salt/roots/", "/srv/salt/"
  #  config.vm.provision :salt do |salt|
  #    salt.minion_config = "salt/minion.conf"
  #    salt.run_highstate = true
  #  end
  #end
  #
end
