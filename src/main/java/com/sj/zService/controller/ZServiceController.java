package com.sj.zService.controller;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.zookeeper.serviceregistry.ServiceInstanceRegistration;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperRegistration;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperServiceRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ZServiceController {

    @Autowired
    private DiscoveryClient discovery;
    @Autowired
    private ZookeeperServiceRegistry serviceRegistry;

    @RequestMapping(value = "/create/{address}/{port}/{zName}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<ServiceInstance>> createZNode(@PathVariable final String address,@PathVariable final Integer port, @PathVariable final String zName) {
        ZookeeperRegistration registration = ServiceInstanceRegistration.builder()
                .defaultUriSpec()
                .address(address)
                .port(port)
                .name(zName)
                .build();
        this.serviceRegistry.register(registration);
        return new ResponseEntity(discovery.getInstances(zName), HttpStatus.OK);
    }


    @RequestMapping(value = "/find/{zName}", method = RequestMethod.GET)
    public ResponseEntity<ServiceInstance> findByServiceName(@PathVariable final String zName) {
        List<ServiceInstance> serviceInstances = discovery.getInstances(zName);
        if(CollectionUtils.isEmpty(serviceInstances)){
            return new ResponseEntity("No ZNode found for zName " + zName, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(discovery.getInstances(zName).get(0), HttpStatus.OK);
    }


    @RequestMapping(value = "/findAll")
    public ResponseEntity<List<String>> findAllServices() {
        return new ResponseEntity(discovery.getServices(), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{zName}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity delete(@PathVariable final String zName) {
        List<ServiceInstance> serviceInstances = discovery.getInstances(zName);
        if(CollectionUtils.isEmpty(serviceInstances)){
            return new ResponseEntity("No ZNode found for zName " + zName, HttpStatus.NOT_FOUND);
        }
        ServiceInstance instance =serviceInstances.get(0);
        ZookeeperRegistration registration = ServiceInstanceRegistration.builder()
                .defaultUriSpec()
                .address(instance.getHost())
                .port(instance.getPort())
                .name(instance.getServiceId())
                .build();
        this.serviceRegistry.deregister(registration);
        return new ResponseEntity(zName, HttpStatus.OK);
    }
}
