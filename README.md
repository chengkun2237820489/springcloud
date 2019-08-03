一、服务的注册与发现Eureka(Finchley版本)

1.1 创建maven主工程
使用idea创建一个maven主工程，选择File->New->Project

不选择Maven骨架点击Next创建Maven工程

添加工程信息，点击Next

选择工作目录，点击Finish，完成工程创建，引入工程依赖
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.3.RELEASE</version>
        <relativePath/>
    </parent>
    <groupId>com.chengkun</groupId>
    <artifactId>springcloud</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>springcloud</name>
    <packaging>pom</packaging>

    <modules>
        <module>eureka-server</module>
        <module>eureka-server1</module>
        <module>eureka-server2</module>
        <module>eureka-client</module>
        <module>service-ribbon</module>
        <module>service-feign</module>
        <module>service-zuul</module>
        <module>config-server</module>
        <module>config-client</module>
        <module>service-hi</module>
        <module>service-miya</module>
        <module>service-turbine</module>
        <module>service-gateway</module>
    </modules>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <spring-cloud.version>Finchley.RELEASE</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>


1.2 创建 Eureka Server 工程
创建一个model工程作为服务注册中心，即Eureka Server,另一个作为Eureka Client。下面以创建server为例子，详细说明创建过程：
右键工程->创建model-> 选择spring initialir 如下图：

点击Next创建如下图所示的模块:

点击Next：

点击Next：

点击Finish，完成Eureka Server的创建，添加server依赖
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.chengkun</groupId>
        <artifactId>cloud</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <relativePath/>
    </parent>
    <groupId>com.chengkun</groupId>
    <artifactId>eureka-server</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>eureka-server</name>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
    </dependencies>
</project>

注册一个Eureka-Server工程，只需在springboot启动类上添加 @EnableEurekaServer
package com.chengkun.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }

}

配置Eureka-Server的配置文件，application.yml
server:
  port: 8761
eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: false
    fetch-registry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/

spring:
  application:
    name: eureka-server


1.3 Eureka Server 是有界面的，启动工程,打开浏览器访问：
http://localhost:8761 ,界面如下：


1.4 创建一个服务提供者 (eureka client)
当client向server注册时，它会提供一些元数据，例如主机和端口，URL，主页等。Eureka server 从每个client实例接收心跳消息。 如果心跳超时，则通常将该实例从注册server中删除。
创建过程同server类似,创建完pom.xml如下：
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.chengkun</groupId>
        <artifactId>cloud</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <relativePath/>
    </parent>
    <groupId>com.chengkun</groupId>
    <artifactId>eureka-client</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>eureka-client</name>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>


需要在配置文件中注明自己的服务注册中心的地址，application.yml配置文件如下：
server:
  port: 8762
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/

spring:
  application:
    name: eureka-client


通过注解@EnableEurekaClient 表明自己是一个eurekaclient.
package com.chengkun.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class EurekaClientApplication {

    package com.chengkun.cloud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class EurekaClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }

    @Value("${server.port}")
    String port;

    @RequestMapping("/hi")
    public String home(@RequestParam(value = "name") String name){
        return "hi " + name + " ,i am from port:" + port;
    }
}


需要指明spring.application.name,这个很重要，这在以后的服务与服务之间相互调用一般都是根据这个name 。
启动工程，打开http://localhost:8761 ，即eureka server 的网址：


打开 http://localhost:8762/hi?name=forezp ，你会在浏览器上看到 :

二、服务消费者（rest+ribbon）(Finchley版本)
2.1 ribbon简介

ribbon是一个负载均衡客户端，可以很好的控制htt和tcp的一些行为。Feign默认集成了ribbon。
ribbon 已经默认实现了这些配置bean：
IClientConfig ribbonClientConfig: DefaultClientConfigImpl
IRule ribbonRule: ZoneAvoidanceRule
IPing ribbonPing: NoOpPing
ServerList ribbonServerList: ConfigurationBasedServerList
ServerListFilter ribbonServerListFilter: ZonePreferenceServerListFilter
ILoadBalancer ribbonLoadBalancer: ZoneAwareLoadBalancer



2.2 建一个服务消费者

重新新建一个模块，取名为：service-ribbon;
在它的pom.xml文件分别引入起步依赖spring-cloud-starter-eureka、spring-cloud-starter-ribbon、spring-boot-starter-web，并继承父pom文件,代码如下：
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.chengkun</groupId>
        <artifactId>springcloud</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <groupId>com.chengkun</groupId>
    <artifactId>service-ribbon</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>service-ribbon</name>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
        </dependency>
    </dependencies>

</project>


在工程的配置文件指定服务的注册中心地址为http://localhost:8761/eureka/，程序名称为 service-ribbon，程序端口为8764。配置文件application.yml如下：
server:
  port: 8764
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/

spring:
  application:
    name: service-ribbon


在工程的启动类中,通过@EnableEurekaClient向服务中心注册；并且向程序的ioc注入一个bean: restTemplate;并通过@LoadBalanced注解表明这个restRemplate开启负载均衡的功能。
注：注解@EnableEurekaClient上有@EnableDiscoveryClient注解区别
如果注册中心是eureka推荐使用@EnableEurekaClient，其他注册注册中心使用@EnableDiscoveryClient
package com.chengkun.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
public class ServiceRibbonApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRibbonApplication.class, args);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}


写一个测试类HelloService，通过之前注入ioc容器的restTemplate来消费eureka-client服务的“/hi”接口，在这里我们直接用的程序名替代了具体的url地址，在ribbon中它会根据服务名来选择具体的服务实例，根据服务实例在请求的时候会用具体的url替换掉服务名，代码如下：
package com.chengkun.cloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author:Administrator
 * @date:2019/7/9
 * @description:
 */
@Service
public class HelloService {

    @Autowired
    RestTemplate restTemplate;

    public String hiService(String name){
        return restTemplate.getForObject("http://eureka-client/hi?name="+name,String.class);
    }
}


写一个controller，在controller中用调用HelloService 的方法，代码如下：
package com.chengkun.cloud.controller;

import com.chengkun.cloud.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:Administrator
 * @date:2019/7/9
 * @description:
 */
@RestController
public class HelloController {
    @Autowired
    HelloService helloService;

    @GetMapping("/hello")
    public String helloController(@RequestParam("name") String name){
        return helloService.hiService(name);
    }
}


在浏览器上多次访问http://localhost:8764/hello?name=chengkun，浏览器交替显示：
hi chengkun ,i am from port:8762
hi chengkun ,i am from port:8763


这说明当我们通过调用restTemplate.getForObject(“http://eureka-client/hi?name=”+name,String.class)方法时，已经做了负载均衡，访问了不同的端口的服务实例。这里使用的是轮询

2.3 此时的架构

一个服务注册中心，eureka server,端口为8761
eureka-client工程跑了两个实例，端口分别为8762,8763，分别向服务注册中心注册
sercvice-ribbon端口为8764,向服务注册中心注册
当sercvice-ribbon通过restTemplate调用eureka-client的hi接口时，因为用ribbon进行了负载均衡，会轮流的调用eureka-client：8762和8763 两个端口的hi接口；
注：
在idea开启两个eureka-client实例，操作如下：
实例1：端口为8762


实例2：端口为8763


三、服务消费者（Feign）(Finchley版本)
3.1 Feign简介
  Feign是一个声明式的伪Http客户端，它使得写Http客户端变得更简单。使用Feign，只需要创建一个接口并注解。它具有可插拔的注解特性，可使用Feign 注解和JAX-RS注解。Feign支持可插拔的编码器和解码器。Feign默认集成了Ribbon，并和Eureka结合，默认实现了负载均衡的效果。

简而言之：

Feign 采用的是基于接口的注解
Feign 整合了ribbon，具有负载均衡的能力
整合了Hystrix，具有熔断的能力

 3.2 创建feign模块
  新建一个spring-boot模块，取名为service-feign，在它的pom文件引入Feign的起步依赖spring-cloud-starter-feign、Eureka的起步依赖spring-cloud-starter-netflix-eureka-client、Web的起步依赖spring-boot-starter-web，代码如下：
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.chengkun</groupId>
        <artifactId>springcloud</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <groupId>com.chengkun</groupId>
    <artifactId>service-feign</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>service-feign</name>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
    </dependencies>

</project>


  在模块的配置文件application.yml文件，指定程序名为service-feign，端口号为8765，服务注册地址为http://localhost:8761/eureka/ ，代码如下：
server:
  port: 8765
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/

spring:
  application:
    name: service-feign



  在程序的启动类ServiceFeignApplication ，加上@EnableFeignClients注解开启Feign的功能：
package com.chengkun.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class ServiceFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceFeignApplication.class, args);
    }

}


  定义一个feign接口，通过@FeignClient（“服务名”），来指定调用哪个服务。比如在代码中调用了eureka-client服务的“/hi”接口，代码如下：
package com.chengkun.cloud.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author chengkun
 * @version v1.0
 * @create 2019/7/9 15:34
 **/
@FeignClient("eureka-client")
public interface SchedualServiceHi {
    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);
}


  在Web层的controller层，对外暴露一个"/hi"的API接口，通过上面定义的Feign客户端SchedualServiceHi 来消费服务。代码如下：
package com.chengkun.cloud.controller;

import com.chengkun.cloud.api.SchedualServiceHi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:Administrator
 * @date:2019/7/9
 * @description:
 */
@RestController
public class HiController {

    @Autowired
    SchedualServiceHi schedualServiceHi;

    @GetMapping("/hi")
    public String hiController(@RequestParam(value = "name") String name){
        return schedualServiceHi.sayHiFromClientOne(name);
    }
}


启动程序，多次访问http://localhost:8765/hi?name=chengkun,浏览器交替显示：
hi chengkun ,i am from port:8762
hi chengkun ,i am from port:8763


四、断路器（Hystrix）
4.1 断路器简介
Netflix开源了Hystrix组件，实现了断路器模式，SpringCloud对这一组件进行了整合。 在微服务架构中，一个请求需要调用多个服务是非常常见的，如下图：

较底层的服务如果出现故障，会导致连锁故障。当对特定的服务的调用的不可用达到一个阀值（Hystric 是5秒20次） 断路器将会被打开。

断路打开后，可用避免连锁故障，fallback方法可以直接返回一个固定值。

4.2 在ribbon使用断路器
改造serice-ribbon 模块的代码，首先在pom.xml文件中加入spring-cloud-starter-netflix-hystrix的起步依赖：
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>


在程序的启动类ServiceRibbonApplication 加@EnableHystrix注解开启Hystrix：
package com.chengkun.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
public class ServiceRibbonApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRibbonApplication.class, args);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}


改造HelloService类，在hiService方法上加上@HystrixCommand注解。该注解对该方法创建了熔断器的功能，并指定了fallbackMethod熔断方法，熔断方法直接返回了一个字符串，字符串为"hi,"+name+",sorry,error!"，代码如下：
package com.chengkun.cloud.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author:Administrator
 * @date:2019/7/9
 * @description:
 */
@Service
public class HelloService {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "hiError")
    public String hiService(String name){
        return restTemplate.getForObject("http://eureka-client/hi?name="+name,String.class);
    }

    public String hiError(String name){
        return "hi,"+name+",sorry,error!";
    }
}


启动：service-ribbon 模块，当我们访问http://localhost:8764/hello?name=chengkun,浏览器显示：
hi chengkun ,i am from port:8762


此时关闭 eureka-client 模块，当我们再访问http://localhost:8764/hello?name=chengkun，浏览器会显示：
hi,chengkun,sorry,error!


这就说明当 eureka-client 模块不可用的时候，service-ribbon调用 eureka-client的API接口时，会执行快速失败，直接返回一组字符串，而不是等待响应超时，这很好的控制了容器的线程阻塞。

4.3 Feign中使用断路器
Feign是自带断路器的，在D版本的Spring Cloud中，它没有默认打开。需要在配置文件中配置打开它，在配置文件加以下代码：
server:
  port: 8765
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/

spring:
  application:
    name: service-feign
feign:
  hystrix:
    enabled: true


基于service-feign模块进行改造，只需要在FeignClient的SchedualServiceHi接口的注解中加上fallback的指定类就行了：
package com.chengkun.cloud.api;

import com.chengkun.cloud.hystric.SchedualServiceHiHystric;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author chengkun
 * @version v1.0
 * @create 2019/7/9 15:34
 **/
@FeignClient(value = "eureka-client",fallback = SchedualServiceHiHystric.class)
public interface SchedualServiceHi {
    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);
}


SchedualServiceHiHystric需要实现SchedualServiceHi 接口，并注入到Ioc容器中，代码如下：
package com.chengkun.cloud.hystric;

import com.chengkun.cloud.api.SchedualServiceHi;
import org.springframework.stereotype.Component;

/**
 * @author:Administrator
 * @date:2019/7/11
 * @description:
 */
@Component
public class SchedualServiceHiHystric implements SchedualServiceHi {
    @Override
    public String sayHiFromClientOne(String name) {
        return "sorry "+name;
    }
}


启动servcie-feign模块，浏览器打开http://localhost:8765/hi?name=chengkun,注意此时eureka-client模块没有启动，网页显示：
sorry chengkun


打开eureka-client模块，再次访问，浏览器显示：
hi chengkun ,i am from port:8762


这证明断路器起到作用了。

4.4 Hystrix Dashboard (断路器：Hystrix 仪表盘)
基于service-ribbon 改造，Feign的改造和这一样。首先在pom.xml引入spring-cloud-starter-netflix-hystrix-dashboard的起步依赖：
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
</dependency>


在主程序启动类中加入@EnableHystrixDashboard注解，开启hystrixDashboard：
在hystrix的2.0之后的版本需要在启动类添加一个servlet
package com.chengkun.cloud;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
@EnableHystrixDashboard
public class ServiceRibbonApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRibbonApplication.class, args);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean
    public ServletRegistrationBean getServlet(){
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/actuator/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }

}


打开浏览器：访问http://localhost:8764/hystrix,界面如下：

点击monitor stream，进入下一个界面，访问：http://localhost:8764/hello?name=chengkun,此时会出现监控界面：


五、路由网关(zuul)(Finchley版本)

在微服务架构中，需要几个基础的服务治理组件，包括服务注册与发现、服务消费、负载均衡、断路器、智能路由、配置管理等，由这几个基础组件相互协作，共同组建了一个简单的微服务系统。一个简单的微服务系统如下图：

注意：A服务和B服务是可以相互调用的，作图的时候忘记了。并且配置服务也是注册到服务注册中心的。

在Spring Cloud微服务系统中，一种常见的负载均衡方式是，客户端的请求首先经过负载均衡（zuul、Ngnix），再到达服务网关（zuul集群），然后再到具体的服。，服务统一注册到高可用的服务注册中心集群，服务的所有的配置文件由配置服务管理（下一篇文章讲述），配置服务的配置文件放在git仓库，方便开发人员随时改配置。

5.1 Zuul简介

Zuul的主要功能是路由转发和过滤器。路由功能是微服务的一部分，比如／api/user转发到到user服务，/api/shop转发到到shop服务。zuul默认和Ribbon结合实现了负载均衡的功能。

zuul有以下功能：
Authentication  ：身份验证
Insights ：监控
Stress Testing ：压力测试
Canary Testing ：金丝雀测试
Dynamic Routing ：动态路由
Service Migration ：服务迁移
Load Shedding ：甩负荷
Security ：安全
Static Response handling ：静态响应处理
Active/Active traffic management ：主动交通管理

5.2 创建service-zuul模块
其pom.xml文件如下：
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.chengkun</groupId>
        <artifactId>springcloud</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <groupId>com.chengkun</groupId>
    <artifactId>service-zuul</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>service-zuul</name>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
        </dependency>
    </dependencies>
</project>


在其入口applicaton类加上注解@EnableZuulProxy，开启zuul的功能：

package com.chengkun.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class ServiceZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceZuulApplication.class, args);
    }

}


加上配置文件application.yml加上以下的配置代码：
server:
  port: 8769
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/

spring:
  application:
    name: service-zuul
zuul:
  routes:
    api-a:
      path: /api-a/**
      serviceId: service-ribbon
    api-b:
      path: /api-b/**
      serviceId: service-feign



首先指定服务注册中心的地址为http://localhost:8761/eureka/，服务的端口为8769，服务名为service-zuul；以/api-a/ 开头的请求都转发给service-ribbon服务；以/api-b/开头的请求都转发给service-feign服务；

依次运行这五个工程;打开浏览器访问：http://localhost:8769/api-a/hello?name=chengkun;浏览器显示：
hi chengkun ,i am from port:8762


打开浏览器访问：http://localhost:8769/api-b/hi?name=chengkun;浏览器显示：
hi chengkun ,i am from port:8762

这说明zuul起到了路由的作用

5.3 服务过滤
zuul不仅只是路由，并且还能过滤，做一些安全验证。继续改造工程；
package com.chengkun.cloud.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author:Administrator
 * @date:2019/7/13
 * @description:
 */
@Component
public class MyFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(MyFilter.class);
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s >>> %s",request.getMethod(),request.getRequestURI()));
        String accessToken = request.getParameter("token");
        if(accessToken == null){
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            try {
                ctx.getResponse().getWriter().write("token is empty");
            } catch (IOException e) {
                log.warn("token is empty");
            }
            return null;
        }
        log.warn("ok");
        return null;
    }
}


注：这个过滤器作用是如果路由没有带token参数，就在页面显示"token is empty",并且阻止页面跳转。
filterType：返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型，具体如下：
pre：路由之前
routing：路由之时
post： 路由之后
error：发送错误调用
filterOrder：过滤的顺序
shouldFilter：这里可以写逻辑判断，是否要过滤，本文true,永远过滤。
run：过滤器的具体逻辑。可用很复杂，包括查sql，nosql去判断该请求到底有没有限访问。

打开浏览器访问：http://localhost:8769/api-a/hello?name=chengkun;浏览器显示：
token is empty


访问：http://localhost:8769/api-a/hello?name=chengkun&token=22 ；
网页显示：
hi chengkun ,i am from port:8762


六、分布式配置中心(Spring Cloud Config)(Finchley版本)

6.1 简介
在分布式系统中，由于服务数量巨多，为了方便服务配置文件统一管理，实时更新，所以需要分布式配置中心组件。在Spring Cloud中，有分布式配置中心组件spring cloud config ，它支持配置服务放在配置服务的内存中（即本地），也支持放在远程Git仓库中。在spring cloud config 组件中，分两个角色，一是config server，二是config client。

6.2 构建Config Server
在父工程添加config-server模块,其pom.xml如下：
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
   <modelVersion>4.0.0</modelVersion>
   <parent>
      <groupId>com.chengkun</groupId>
      <artifactId>springcloud</artifactId>
      <version>0.0.1-SNAPSHOT</version>
   </parent>
   <groupId>com.chengkun</groupId>
   <artifactId>config-server</artifactId>
   <version>0.0.1-SNAPSHOT</version>
   <name>config-server</name>
   <packaging>jar</packaging>


   <dependencies>
      <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-web</artifactId>
      </dependency>
      <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-config-server</artifactId>
      </dependency>
   </dependencies>

</project>


在程序的入口Application类加上@EnableConfigServer注解开启配置服务器的功能，代码如下：
package com.chengkun.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

   public static void main(String[] args) {
      SpringApplication.run(ConfigServerApplication.class, args);
   }

}


需要在程序的配置文件application.properties文件配置以下：
spring.application.name=config-server
server.port=8888

spring.cloud.config.server.git.uri=https://gitee.com/i_have_dream/springcloudconfig/
spring.cloud.config.server.git.searchPaths=respo
spring.cloud.config.label=master
spring.cloud.config.server.git.username=
spring.cloud.config.server.git.password=


spring.cloud.config.server.git.uri：配置git仓库地址
spring.cloud.config.server.git.searchPaths：配置仓库路径
spring.cloud.config.label：配置仓库的分支
spring.cloud.config.server.git.username：访问git仓库的用户名
spring.cloud.config.server.git.password：访问git仓库的用户密码

如果Git仓库为公开仓库，可以不填写用户名和密码，如果是私有仓库需要填写，本例子是公开仓库，放心使用。

远程仓库https://gitee.com/i_have_dream/springcloudconfig/ 中有个文件config-client-dev.properties文件中有一个属性：
foo = foo version 1


启动程序：访问http://localhost:8888/foo/dev

证明配置服务中心可以从远程程序获取配置信息。
http请求地址和资源文件映射如下:
/{application}/{profile}[/{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties

注：所有程序都需要启动eureka-server服务，会自动查找服务，否则会出现Cannot execute request on any known server异常
6.3 构建一个config client

在父工程添加config-client模块,其pom.xml如下：
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.chengkun</groupId>
        <artifactId>springcloud</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <groupId>com.chengkun</groupId>
    <artifactId>config-client</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>config-client</name>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>

</project>


其配置文件bootstrap.properties：
spring.application.name=config-client
spring.cloud.config.label=master
spring.cloud.config.profile=dev
spring.cloud.config.uri= http://localhost:8888/
server.port=8881

spring.cloud.config.label 指明远程仓库的分支
spring.cloud.config.profile
dev开发环境配置文件
test测试环境
pro正式环境
spring.cloud.config.uri= http://localhost:8888/ 指明配置服务中心的网址。
程序的入口类，写一个API接口“／hi”，返回从配置中心读取的foo变量的值，代码如下：
package com.chengkun.cloud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ConfigClientApplication {

    @Value("${foo}")
    String foo;

    public static void main(String[] args) {
        SpringApplication.run(ConfigClientApplication.class, args);
    }

    @RequestMapping("/hi")
    private String hi(){
        return foo;
    }
}


打开网址访问：http://localhost:8881/hi，网页显示：
foo version 1

注：不要在git上config-client-dev.properties文件添加与client不同的端口号
经过测试:
    server.port=8881 成功
    server.port=8882 失败
    不写端口号也成功


这就说明，config-client从config-server获取了foo的属性，而config-server是从git仓库读取的,如图：


七、高可用的分布式配置中心(Spring Cloud Config)(Finchley版本)

服务如何从配置中心读取文件，配置中心如何从远程git读取配置文件，当服务实例很多时，都从配置中心读取文件，这时可以考虑将配置中心做成一个微服务，将其集群化，从而达到高可用，架构图如下：


7.1 改造config-server

将之前的eureka-server作为服务注册中心。现改造config-server作为eureka-client，在其pom.xml文件加上EurekaClient的起步依赖spring-cloud-starter-netflix-eureka-client，代码如下:
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
   <modelVersion>4.0.0</modelVersion>
   <parent>
      <groupId>com.chengkun</groupId>
      <artifactId>springcloud</artifactId>
      <version>0.0.1-SNAPSHOT</version>
   </parent>
   <groupId>com.chengkun</groupId>
   <artifactId>config-server</artifactId>
   <version>0.0.1-SNAPSHOT</version>
   <name>config-server</name>
   <packaging>jar</packaging>


   <dependencies>
      <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-web</artifactId>
      </dependency>
      <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-config-server</artifactId>
      </dependency>
      <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
      </dependency>
   </dependencies>

</project>


配置文件application.yml，指定服务注册地址为http://localhost:8761/eureka/，其他配置同上一篇文章，完整的配置如下：
spring.application.name=config-server
server.port=8888

spring.cloud.config.server.git.uri=https://gitee.com/i_have_dream/springcloudconfig/
spring.cloud.config.server.git.searchPaths=respo
spring.cloud.config.label=master
spring.cloud.config.server.git.username=
spring.cloud.config.server.git.password=
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/


最后需要在程序的启动类Application加上@EnableEureka的注解。配置如下：
package com.chengkun.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableConfigServer
@EnableEurekaClient
public class ConfigServerApplication {

   public static void main(String[] args) {
      SpringApplication.run(ConfigServerApplication.class, args);
   }

}


7.2 改造config-client
将其注册微到服务注册中心，作为Eureka客户端，需要pom文件加上起步依赖spring-cloud-starter-netflix-eureka-client，代码如下：
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.chengkun</groupId>
        <artifactId>springcloud</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <groupId>com.chengkun</groupId>
    <artifactId>config-client</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>config-client</name>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
    </dependencies>

</project>


配置文件bootstrap.properties，注意是bootstrap。加上服务注册地址为http://localhost:8761/eureka/
spring.application.name=config-client
spring.cloud.config.label=master
spring.cloud.config.profile=dev
#spring.cloud.config.uri= http://localhost:8888/
server.port=8881
spring.cloud.config.discovery.enabled=true
spring.cloud.config.discovery.serviceId=config-server
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/


spring.cloud.config.discovery.enabled 是从配置中心读取文件。
spring.cloud.config.discovery.serviceId 配置中心的servieId，即服务名。

这时发现，在读取配置文件不再写ip地址，而是服务名，这时如果配置服务部署多份，通过负载均衡，从而高可用。

最后需要在程序的启动类Application加上@EnableEureka的注解。配置如下：
package com.chengkun.cloud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableEurekaClient
public class ConfigClientApplication {

    @Value("${foo}")
    String foo;

    public static void main(String[] args) {
        SpringApplication.run(ConfigClientApplication.class, args);
    }

    @RequestMapping("/hi")
    private String hi(){
        return foo;
    }
}


依次启动eureka-server,config-server,config-client
访问网址：http://localhost:8761/

访问http://localhost:8881/hi，浏览器显示：
foo version 1


八、消息总线(Spring Cloud Bus)(Finchley版本)
Spring Cloud Bus 将分布式的节点用轻量的消息代理连接起来。它可以用于广播配置文件的更改或者服务之间的通讯，也可以用于监控。本文要讲述的是用Spring Cloud Bus实现通知微服务架构的配置文件的更改。

按照官方文档，我们只需要在配置文件中配置 spring-cloud-starter-bus-amqp ；这就是说我们需要装rabbitMq，点击rabbitmq下载。至于怎么使用 rabbitmq，搜索引擎下。

8.1 改造config-client
在pom文件加上起步依赖spring-cloud-starter-bus-amqp，完整的配置文件如下：
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.chengkun</groupId>
        <artifactId>springcloud</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <groupId>com.chengkun</groupId>
    <artifactId>config-client</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>config-client</name>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.retry</groupId>
            <artifactId>spring-retry</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
    </dependencies>

</project>


在配置文件bootstrap.properties中加上RabbitMq的配置，包括RabbitMq的地址、端口，用户名、密码，代码如下：
spring.application.name=config-client
spring.cloud.config.label=master
spring.cloud.config.profile=dev
#spring.cloud.config.uri= http://localhost:8888/
server.port=8881
spring.cloud.config.discovery.enabled=true
spring.cloud.config.discovery.serviceId=config-server
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
# spring.rabbitmq.username=
# spring.rabbitmq.password=


如果rabbitmq有用户名密码，输入即可。
依次启动eureka-server、confg-server,启动两个config-client，端口为：8881、8882。
配置两个config-client如下：
ConfigClientApplication1 端口号：8881

ConfigClientApplication2 端口号：8882


访问http://localhost:8881/hi 或者http://localhost:8882/hi 浏览器显示：
foo version 1

这时我们去代码仓库将foo的值改为“foo version 2”，即改变配置文件foo的值。如果是传统的做法，需要重启服务，才能达到配置文件的更新。此时，我们只需要发送post请求：http://localhost:8881/bus/refresh，你会发现config-client会重新读取配置文件
这时我们再访问http://localhost:8881/hi 或者http://localhost:8882/hi 浏览器显示：
foo version 2


另外，/bus/refresh接口可以指定服务，即使用"destination"参数，比如 “/bus/refresh?destination=customers:**” 即刷新服务名为customers的所有服务，不管ip。

8.2 分析
此时的架构图：

当git文件更改的时候，通过pc端用post 向端口为8882的config-client发送请求/bus/refresh／；此时8882端口会发送一个消息，由消息总线向其他服务传递，从而使整个微服务集群都达到更新配置文件。

8.3 其他扩展（可忽视）
可以用作自定义的Message Broker,只需要spring-cloud-starter-bus-amqp, 然后再配置文件写上配置即可，同上。
Tracing Bus Events：
需要设置：spring.cloud.bus.trace.enabled=true，如果那样做的话，那么Spring Boot TraceRepository（如果存在）将显示每个服务实例发送的所有事件和所有的ack,比如：（来自官网）
{
  "timestamp": "2015-11-26T10:24:44.411+0000",
  "info": {
    "signal": "spring.cloud.bus.ack",
    "type": "RefreshRemoteApplicationEvent",
    "id": "c4d374b7-58ea-4928-a312-31984def293b",
    "origin": "stores:8081",
    "destination": "*:**"
  }
  },
  {
  "timestamp": "2015-11-26T10:24:41.864+0000",
  "info": {
    "signal": "spring.cloud.bus.sent",
    "type": "RefreshRemoteApplicationEvent",
    "id": "c4d374b7-58ea-4928-a312-31984def293b",
    "origin": "customers:9000",
    "destination": "*:**"
  }
  },
  {
  "timestamp": "2015-11-26T10:24:41.862+0000",
  "info": {
    "signal": "spring.cloud.bus.ack",
    "type": "RefreshRemoteApplicationEvent",
    "id": "c4d374b7-58ea-4928-a312-31984def293b",
    "origin": "customers:9000",
    "destination": "*:**"
  }
}


九、第九篇: 服务链路追踪(Spring Cloud Sleuth)(Finchley版本)

9.1 简介
Spring Cloud Sleuth 主要功能就是在分布式系统中提供追踪解决方案，并且兼容支持了 zipkin，你只需要在pom文件中引入相应的依赖即可。

9.2 服务追踪分析
微服务架构上通过业务来划分服务的，通过REST调用，对外暴露的一个接口，可能需要很多个服务协同才能完成这个接口功能，如果链路上任何一个服务出现问题或者网络超时，都会形成导致接口调用失败。随着业务的不断扩张，服务之间互相调用会越来越复杂。

9.3 术语

Span：基本工作单元，例如，在一个新建的span中发送一个RPC等同于发送一个回应请求给RPC，span通过一个64位ID唯一标识，trace以另一个64位ID表示，span还有其他数据信息，比如摘要、时间戳事件、关键值注释(tags)、span的ID、以及进度ID(通常是IP地址)
span在不断的启动和停止，同时记录了时间信息，当你创建了一个span，你必须在未来的某个时刻停止它。
Trace：一系列spans组成的一个树状结构，例如，如果你正在跑一个分布式大数据工程，你可能需要创建一个trace。
Annotation：用来及时记录一个事件的存在，一些核心annotations用来定义一个请求的开始和结束
cs - Client Sent -客户端发起一个请求，这个annotion描述了这个span的开始
sr - Server Received -服务端获得请求并准备开始处理它，如果将其sr减去cs时间戳便可得到网络延迟
ss - Server Sent -注解表明请求处理的完成(当请求返回客户端)，如果ss减去sr时间戳便可得到服务端需要的处理请求时间
cr - Client Received -表明span的结束，客户端成功接收到服务端的回复，如果cr减去cs时间戳便可得到客户端从服务端获取回复的所有所需时间

将Span和Trace在一个系统中使用Zipkin注解的过程图形化：

9.4 构建工程
主要有三个工程组成:一个server-zipkin,它的主要作用使用ZipkinServer 的功能，收集调用数据，并展示；一个service-hi,对外暴露hi接口；一个service-miya,对外暴露miya接口；这两个service可以相互调用；并且只有调用了，server-zipkin才会收集数据的，这就是为什么叫服务追踪了。

构建server-zipkin：
在spring Cloud为F版本的时候，已经不需要自己构建Zipkin Server了，只需要下载jar即可，下载地址：

https://dl.bintray.com/openzipkin/maven/io/zipkin/java/zipkin-server/

也可以在这里下载：

链接:https://pan.baidu.com/s/1w614Z8gJXHtqLUB6dKWOpQ 密码: 26pf

下载完成jar 包之后，需要运行jar，进入jar包位置，打开命令窗口，输入命令，如下：
java -jar zipkin-server-2.10.2-exec.jar


访问浏览器localhost:9411


9.5 创建service-hi
在其pom引入起步依赖spring-cloud-starter-zipkin，代码如下：
spring:
  zipkin:
    base-url: http://localhost:9411
  application:
    name: service-hi
server:
  port: 8988


通过引入spring-cloud-starter-zipkin依赖和设置spring.zipkin.base-url就可以了。
对外暴露接口：
启动类：
package com.chengkun.cloud;

import brave.sampler.Sampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ServiceHiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceHiApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    @Bean
    //安装采样器,设置可以导出所有内容
    public Sampler defaultSampler(){
        return Sampler.ALWAYS_SAMPLE;
    }
}


controlller层：
package com.chengkun.cloud.controller;


import com.chengkun.cloud.ServiceHiApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author:Administrator
 * @date:2019/7/15
 * @description:
 */
@RestController
public class ServiceHiController {

    private static final Logger LOG = Logger.getLogger(ServiceHiApplication.class.getName());

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/hi")
    public String callHome(){
        LOG.log(Level.INFO, "calling trace service-hi  ");
        return restTemplate.getForObject("http://localhost:8989/miya",String.class);
    }

    @RequestMapping("/info")
    public String info(){
        LOG.log(Level.INFO, "calling trace service-hi ");
        return "i'm service-hi";
    }
}


9.6 创建service-miya
创建过程同service-hi，引入相同的依赖，配置下spring.zipkin.base-url。
对外暴露接口：
启动类：
package com.chengkun.cloud;

import brave.sampler.Sampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ServiceMiyaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceMiyaApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    @Bean
    //安装采样器,设置可以导出所有内容
    public Sampler defaultSampler(){
        return Sampler.ALWAYS_SAMPLE;
    }
}


controller层：
package com.chengkun.cloud.controller;

import com.chengkun.cloud.ServiceMiyaApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author:Administrator
 * @date:2019/7/15
 * @description:
 */
@RestController
public class ServiceMiyaController {

    @Autowired
    RestTemplate restTemplate;

    private static final Logger LOG = Logger.getLogger(ServiceMiyaApplication.class.getName());

    @RequestMapping("/miya")
    public String info(){
        LOG.log(Level.INFO, "info is being called");
        return restTemplate.getForObject("http://localhost:8988/info",String.class);
    }

    @RequestMapping("/hi")
    public String home(){
        LOG.log(Level.INFO, "hi is being called");
        return "hi i'm miya!";
    }
}


9.7 启动工程，演示追踪
依次启动上面的工程，打开浏览器访问：http://localhost:9411/，会出现以下界面：



访问：http://localhost:8989/miya，浏览器出现：
i'm service-hi


再打开http://localhost:9411/的界面，点击依赖分析,可以发现服务的依赖关系：

点击查找调用链,可以看到具体服务相互调用的数据：

十、高可用的服务注册中心(Finchley版本)
Eureka通过运行多个实例，使其更具有高可用性。事实上，这是它默认的熟性，你需要做的就是给对等的实例一个合法的关联serviceurl。

10.1 创建eureka-server1
实现高可用的服务注册中心就是使用多个注册中心进行相互注册，客户端在调用时可以注册多个服务中心，当某一台注册中心宕机不会影响整个服务的宕机，两台服务注册中心信息是一致的。
创建过程同eureka-server类似,创建完pom.xml如下：
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.chengkun</groupId>
        <artifactId>springcloud</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <relativePath/>
    </parent>
    <groupId>com.chengkun</groupId>
    <artifactId>eureka-server1</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>eureka-server1</name>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
    </dependencies>
</project>


修改hosts文件，windows电脑，在c:/windows/systems/drivers/etc/hosts 修改，linux系统通过vim /etc/hosts ,加上：
127.0.0.1	peer1
127.0.0.1	peer2


application.yml配置文件：
server:
  port: 8761
eureka:
  instance:
    hostname: peer1
  client:
    allow-redirects: true
    fetch-registry: true
    serviceUrl:
      defaultZone: http://peer2:8769/eureka/  #其它注册中心地址，如果有多个用逗号隔开

spring:
  application:
    name: eureka-server1


10.2 创建eureka-server2模块
创建与eureka-server1一样的模块，pom文件如下：
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.chengkun</groupId>
        <artifactId>springcloud</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <groupId>com.chengkun</groupId>
    <artifactId>eureka-server2</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>eureka-server2</name>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
    </dependencies>
</project>

主要修改application.yml文件：
server:
  port: 8769
eureka:
  instance:
    hostname: peer2
  client:
    allow-redirects: true
    fetch-registry: true
    serviceUrl:
      defaultZone: http://peer1:8761/eureka/  #其它注册中心地址，如果有多个用逗号隔开

spring:
  application:
    name: eureka-server2



启动类：
package com.chengkun.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServer2Application {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServer2Application.class, args);
    }

}


这时需要改造下service-hi，只需修改注册中心地址，填写两个地址:
server:
  port: 8762
eureka:
  client:
    serviceUrl:
      defaultZone: http://peer1:8761/eureka/,http://peer2:8769/eureka/

spring:
  application:
    name: eureka-client



启动eureka-server1、eureka-server2、eureka-client1,访问：localhost:8761,如图：

访问localhost:8769，如图：

访问http://localhost:8762/hi?name=chengkun页面显示：
hi chengkun ,i am from port:8762


当停止eureka-server2时，再次访问http://localhost:8762/hi?name=chengkun页面显示：
hi chengkun ,i am from port:8762

Eureka-eserver peer1 8761,Eureka-eserver peer2 8769相互感应，当有服务注册时，两个Eureka-server是对等的，它们都存有相同的信息，这就是通过服务器的冗余来增加可靠性，当有一台服务器宕机了，服务并不会终止，因为另一台服务存有相同的数据。
此时的架构图：


十一、docker部署spring cloud项目
11.1 docker简介
Docker是一个开源的引擎，可以轻松的为任何应用创建一个轻量级的、可移植的、自给自足的容器。开发者在笔记本上编译测试通过的容器可以批量地在生产环境中部署，包括VMs（虚拟机）、bare metal、OpenStack 集群和其他的基础应用平台。
Docker通常用于如下场景：
web应用的自动化打包和发布；
自动化测试和持续集成、发布；
在服务型环境中部署和调整数据库或其他的后台应用；
从头编译或者扩展现有的OpenShift或Cloud Foundry平台来搭建自己的PaaS环境。
Docker 的优点：
1、简化程序：
Docker 让开发者可以打包他们的应用以及依赖包到一个可移植的容器中，然后发布到任何流行的 Linux 机器上，便可以实现虚拟化。Docker改变了虚拟化的方式，使开发者可以直接将自己的成果放入Docker中进行管理。方便快捷已经是 Docker的最大优势，过去需要用数天乃至数周的	任务，在Docker容器的处理下，只需要数秒就能完成。
2、避免选择恐惧症：
如果你有选择恐惧症，还是资深患者。Docker 帮你	打包你的纠结！比如 Docker 镜像；Docker 镜像中包含了运行环境和配置，所以 Docker 可以简化部署多种应用实例工作。比如 Web 应用、后台应用、数据库应用、大数据应用比如 Hadoop 集群、消息队列等等都可以打包成一个镜像部署。
3、节省开支：
一方面，云计算时代到来，使开发者不必为了追求效果而配置高额的硬件，Docker 改变了高性能必然高价格的思维定势。Docker 与云的结合，让云空间得到更充分的利用。不仅解决了硬件管理的问题，也改变了虚拟化的方式。

关于docker 的安装和基本的使用见相关教程。

11.2 改造工程、构建镜像

第一步安装docker插件：


第二步：重启idea后打开settings查看docker并新建连接：



如果是远程服务器的ip，另外要想连接成功得先让服务器开启2375端口，详细操作进入docker开启远程访问,连接成功后，点击ok出现下图


双击Docker出现


接下来我们在idea上面操作docker
我用Eureka-server作为本次实验的springcloud项目，我们的目的就是要把该项目放到docker上面跑起来
在src/main下面新建一个docker文件夹，目录下新建一个Dokerfile文件（就是新建一个file，名字写Dockerfile），写入一下的内容：
FROM java:8
VOLUME /tmp
ADD eureka-server-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8761
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]


VOLUME 指定了临时文件目录为/tmp。其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp。改步骤是可选的，如果涉及到文件系统的应用就很有必要了。/tmp目录用来持久化到 Docker 数据文件夹，因为 Spring Boot 使用的内嵌 Tomcat 容器默认使用/tmp作为工作目录 
项目的 jar 文件作为 “app.jar” 添加到容器的 
ENTRYPOINT 执行项目 app.jar。为了缩短 Tomcat 启动时间，添加一个系统属性指向 “/dev/urandom” 作为 Entropy Source 
ADD eureka-server-0.0.1-SNAPSHOT.jar app.jar 中eureka-server是工程名，8761是该工程的端口号
然后设置启动项，选择dockerfile




这个时候我重新布置一下pom文件，代码如下：
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.chengkun</groupId>
        <artifactId>springcloud</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <groupId>com.chengkun</groupId>
    <artifactId>eureak-server</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>eureka-server</name>
    <packaging>jar</packaging>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <spring-cloud.version>Finchley.RELEASE</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>com.spotify</groupId>
                <artifactId>docker-maven-plugin</artifactId>
                <version>0.4.3</version>
                <!--将插件绑定在某个phase执行-->
                <executions>
                    <execution>
                        <id>build-image</id>
                        <!--将插件绑定在package这个phase上。也就是说，用户只需执行mvn package ，就会自动执行mvn docker:build-->
                        <phase>package</phase>
                        <goals>
                            <goal>build</goal>
                        </goals>
                    </execution>
                </executions>
                <!-- tag::plugin[] -->
                <configuration>
                    <!--指定生成的镜像名-->
                    <imageName>eureka-server</imageName>
                    <!--指定标签-->
                    <imageTags>
                        <imageTag>latest</imageTag>
                    </imageTags>
                    <!-- 指定 Dockerfile 路径-->
                    <dockerDirectory>${project.basedir}/src/main/docker</dockerDirectory>
                    <!--指定远程 docker api地址-->
                    <dockerHost>http://39.96.172.135:2375</dockerHost>
                    <!-- 这里是复制 jar 包到 docker 容器指定目录配置 -->
                    <resources>
                        <resource>
                            <targetPath>/</targetPath>
                            <!--jar 包所在的路径  此处配置的 即对应 target 目录-->
                            <directory>${project.build.directory}</directory>
                            <!-- 需要包含的 jar包 ，这里对应的是 Dockerfile中添加的文件名　-->
                            <include>${project.build.finalName}.jar</include>
                        </resource>
                    </resources>
                    <!-- 以下两行是为了docker push到DockerHub使用的。 -->
                    <!--<serverId>docker-hub</serverId>
                    <registryUrl>https://index.docker.io/v1</registryUrl>-->
                    <!--<serverId>deploymentRepo</serverId>-->
                </configuration>
                <!-- end::plugin[] -->
            </plugin>
        </plugins>
    </build>
</project>

Spotify 的 docker-maven-plugin 插件是用maven插件方式构建docker镜像的。
imageName指定了镜像的名字，本例为 ck-eureka-server1
dockerDirectory指定 Dockerfile 的位置
resources是指那些需要和 Dockerfile 放在一起，在构建镜像时使用的文件，一般应用 jar 包需要纳入。

关于下面这部分代码,是将maven与docker进行绑定，我们可以直接maven打jar包，然后绑定运行dockerfile文件。
<!--将插件绑定在某个phase执行-->
<executions>
    <execution>
        <id>build-image</id>
        <!--将插件绑定在package这个phase上。也就是说，用户只需执行mvn package ，就会自动执行mvn docker:build-->
        <phase>package</phase>
        <goals>
            <goal>build</goal>
        </goals>
    </execution>
</executions>


然后使用maven打jar包：


一键，生成镜像文件。


镜像生成了，下一步生产容器：


然后出现下面的对话框：





HostPort是docker开放的访问端口，Container port是映射到docker内部的端口，我写的分别是6000，8761，其中8761就是springcloud的appliction.yml里的server.port的值
启动容器就可以访问服务器的程序了


十二、断路器聚合监控(Hystrix Turbine)

12.1 Hystrix Turbine简介
看单个的Hystrix Dashboard的数据并没有什么多大的价值，要想看这个系统的Hystrix Dashboard数据就需要用到Hystrix Turbine。Hystrix Turbine将每个服务Hystrix Dashboard数据进行了整合。Hystrix Turbine的使用非常简单，只需要引入相应的依赖和加上注解和配置就可以了。

12.2 创建service-turbine
引入相应的依赖：
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.chengkun</groupId>
        <artifactId>springcloud</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <groupId>com.chengkun</groupId>
    <artifactId>service-turbine</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>service-turbine</name>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-netflix-turbine</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>


在其入口类ServiceTurbineApplication加上注解@EnableTurbine，开启turbine，@EnableTurbine注解包含了@EnableDiscoveryClient注解，即开启了注册服务。
package com.chengkun.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

@SpringBootApplication
@EnableTurbine
public class ServiceTurbineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceTurbineApplication.class, args);
    }

}


配置文件application.yml：
spring:
  application.name: service-turbine
server:
  port: 8766
turbine:
  aggregator:
    clusterConfig: default   # 指定聚合哪些集群，多个使用","分割，默认为default。可使用http://.../turbine.stream?cluster={clusterConfig之一}访问
  appConfig: service-ribbon,service-feign  # 配置Eureka中的serviceId列表，表明监控哪些服务
  clusterNameExpression: new String("default")
  # 1. clusterNameExpression指定集群名称，默认表达式appName；此时：turbine.aggregator.clusterConfig需要配置想要监控的应用名称
  # 2. 当clusterNameExpression: default时，turbine.aggregator.clusterConfig可以不写，因为默认就是default
  # 3. 当clusterNameExpression: metadata['cluster']时，假设想要监控的应用配置了eureka.instance.metadata-map.cluster: ABC，则需要配置，同时turbine.aggregator.clusterConfig: ABC
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/


12.3 Turbine演示
依次开启eureka-server、service-client1、service-ribbon、service-feign、service-turbine工程。
打开浏览器输入：http://localhost:8766/turbine.stream,界面如下：

依次请求：
http://localhost:8764/hello?name=chengkun
http://localhost:8765/hi?name=chengkun
打开:http://localhost:8764/hystrix,输入监控流http://localhost:8766/turbine.stream


可以看到这个页面聚合了2个service的hystrix dashbord数据。


十三、Spring Cloud Gateway初体验
Spring Cloud Gateway是Spring Cloud官方推出的第二代网关框架，取代Zuul网关。网关作为流量的，在微服务系统中有着非常作用，网关常见的功能有路由转发、权限校验、限流控制等作用。本文首先用官方的案例带领大家来体验下Spring Cloud的一些简单的功能，在后续文章我会使用详细的案例和源码解析来详细讲解Spring Cloud Gateway.

13.1 创建service-gateway模块
新建一个模块，取名为sevice-gateway在工程的pom文件引用工程所需的依赖，包括spring boot和spring cloud，以及gateway的起步依赖spring-cloud-starter-gateway，代码如下：
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.chengkun</groupId>
        <artifactId>springcloud</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <groupId>com.chengkun</groupId>
    <artifactId>service-gateway</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>service-gateway</name>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
    </dependencies>
</project>


13.2 创建一个简单的路由
在spring cloud gateway中使用RouteLocator的Bean进行路由转发，将请求进行处理，最后转发到目标的下游服务。我们将请求转发到http://www.baidu.com:80这个地址上。代码如下：
package com.chengkun.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ServiceGatewayApplication {

    @Autowired
    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(ServiceGatewayApplication.class, args);
    }

    @Bean
    public RouteLocatorBuilder routeLocatorBuilder() {
        return new RouteLocatorBuilder(context);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/get")
                        .filters(f -> f.addRequestHeader("Hello", "World"))
                        .uri("http://httpbin.org:80"))
                .build();
    }
}


在上面的myRoutes方法中，使用了一个RouteLocatorBuilder的bean去创建路由，除了创建路由RouteLocatorBuilder可以让你添加各种predicates和filters，predicates断言的意思，顾名思义就是根据具体的请求的规则，由具体的route去处理，filters是各种过滤器，用来对请求做各种判断和修改。
上面创建的route可以让请求“/get”请求都转发到“http://httpbin.org/get”。在route配置上，我们添加了一个filter，该filter会将请求添加一个header,key为hello，value为world。
启动springboot项目，在浏览器上http://localhost:8080/get，浏览器显示如下:
{
  "args": {}, 
  "headers": {
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8", 
    "Accept-Encoding": "gzip, deflate, br", 
    "Accept-Language": "zh-CN,zh;q=0.9", 
    "Cookie": "IS_LOGIN_MAIN=true; curMenu=true; pageNo=1; pageSize=10; insight.session.id=9a28a0d5f9334909aab6015dade994f9; JSESSIONID=C6A516829E67EA9820E32ECD2E3B65A5", 
    "Forwarded": "proto=http;host=\"localhost:8080\";for=\"0:0:0:0:0:0:0:1:51337\"", 
    "Hello": "World", 
    "Host": "httpbin.org", 
    "Upgrade-Insecure-Requests": "1", 
    "User-Agent": "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36", 
    "X-Forwarded-Host": "localhost:8080"
  }, 
  "origin": "0:0:0:0:0:0:0:1, 36.7.144.234, ::1", 
  "url": "https://localhost:8080/get"
}


注意HTTPBin展示了请求的header hello和值world。

13.3 使用Hystrix
在spring cloud gateway中可以使用Hystrix。Hystrix是 spring cloud中一个服务熔断降级的组件，在微服务系统有着十分重要的作用。
Hystrix是 spring cloud gateway中是以filter的形式使用的，代码如下：
@Bean
public RouteLocator myRoutes(RouteLocatorBuilder builder) {
    String httpUri = "http://httpbin.org:80";
    return builder.routes()
            .route(p -> p
                    .path("/get")
                    .filters(f -> f.addRequestHeader("Hello", "World"))
                    .uri(httpUri))
            .route(p -> p
                    .host("*.hystrix.com")
                    .filters(f -> f
                            .hystrix(config -> config
                            .setName("mycmd")
                                    .setFallbackUri("forward:/fallback")))
                    .uri(httpUri))
            .build();
}


在上面的代码中，我们使用了另外一个router，该router使用host去断言请求是否进入该路由，当请求的host有“*.hystrix.com”，都会进入该router，该router中有一个hystrix的filter,该filter可以配置名称、和指向性fallback的逻辑的地址，比如本案例中重定向到了“/fallback”。
现在写的一个“/fallback”的逻辑：
@RequestMapping("/fallback")
public Mono<String> fallback() {
    return Mono.just("fallback");
}

Mono是一个Reactive stream，对外输出一个“fallback”字符串。
使用curl执行以下命令：
curl --dump-header - --header Host:www.hystrix.com http://localhost:8080/delay/1

返回的响应为：
fallback


可见，带host:www.hystrix.com的请求执行了hystrix的fallback的逻辑。

十四、Spring Cloud Gateway之Predict

14.1 Spring Cloud gateway工作流程
Spring Cloud Gateway在开发中主要起到网关的作用，通常的作用如下：
协议转换，路由转发
流量聚合，对流量进行监控，日志输出
作为整个系统的前端工程，对流量进行控制，有限流的作用
作为系统的前端边界，外部流量只能通过网关才能访问系统
可以在网关层做权限的判断
可以在网关层做缓存
Spring Cloud Gateway作为Spring Cloud框架的第二代网关，在功能上要比Zuul更加的强大，性能也更好。随着Spring Cloud的版本迭代，Spring Cloud官方有打算弃用Zuul的意思。在笔者调用了Spring Cloud Gateway的使用和功能上，Spring Cloud Gateway替换掉Zuul的成本上是非常低的，几乎可以无缝切换。Spring Cloud Gateway几乎包含了zuul的所有功能。

大致流程：
请求去访问GateWay Client
访问GateWay Handler Mappeing 寻找有没有与路径相匹配的路由
根据predicate来决定访问那个路由
再经过pre类型的过滤器，进行请求代理
再经过post类型过滤器

14.2 predicate简介
Spring Cloud Gateway内置了许多Predict,这些Predict的源码在org.springframework.cloud.gateway.handler.predicate包中，如下图：

在上图中，有很多类型的Predicate,比如说时间类型的Predicated（AfterRoutePredicateFactory BeforeRoutePredicateFactory BetweenRoutePredicateFactory），当只有满足特定时间要求的请求会进入到此predicate中，并交由router处理；cookie类型的CookieRoutePredicateFactory，指定的cookie满足正则匹配，才会进入此router;以及host、method、path、querparam、remoteaddr类型的predicate，每一种predicate都会对当前的客户端请求进行判断，是否满足当前的要求，如果满足则交给当前请求处理。如果有很多个Predicate，并且一个请求满足多个Predicate，则按照配置的顺序第一个生效。

14.3 predicate实战
创建一个模块service-gateway-predicate，在pom文件引入spring cloud gateway 的起步依赖spring-cloud-starter-gateway，spring cloud版本和spring boot版本，代码如下：
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.chengkun</groupId>
        <artifactId>springcloud</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <groupId>com.chengkun</groupId>
    <artifactId>service-gateway-predicate</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>service-gateway-predicate</name>
    <packaging>jar</packaging>


    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
    </dependencies>
</project>


Datetime 类型的路由谓词工厂有三种，分别为：
AfterRoutePredicateFactory： 接收一个日期参数，判断请求日期是否晚于指定日期
BeforeRoutePredicateFactory： 接收一个日期参数，判断请求日期是否早于指定日期
BetweenRoutePredicateFactory： 接收两个日期参数，判断请求日期是否在指定时间段内

After Route Predicate Factory

AfterRoutePredicateFactory，可配置一个时间，当请求的时间在配置时间之后，才交给 router去处理。否则则报错，不通过路由。
在工程的application.yml配置如下：
server:
  port: 8081
spring:
  profiles:
    active: after_route
---
spring:
  cloud:
    gateway:
      routes:
        - id: after_route
          uri:  http://www.baidu.com
          predicates:
            - After= 2019-07-30T23:59:59.789+08:00[Asia/Shanghai]
  profiles: after_route


在上面的配置文件中，配置了服务的端口为8081，配置spring.profiles.active:after_route指定了程序的spring的启动文件为after_route文件。在application.yml再建一个配置文件，语法是三个横线，在此配置文件中通过spring.profiles来配置文件名，和spring.profiles.active一致，然后配置spring cloud gateway 相关的配置，id标签配置的是router的id，每个router都需要一个唯一的id，uri配置的是将请求路由到哪里，本案例全部路由到http://www.baidu.com。

predicates:
  - After= 2019-07-30T23:59:59.789+08:00[Asia/Shanghai]

上面的配置文件指定了路由的断言，谓词关键字是 After ，表示请求时间必须晚于上海时间 2019年7月30日 23:59:59 才可用
启动工程，在浏览器上访问http://localhost:8081/，会显示http://www.baidu.com返回的结果，此时gateway路由到了配置的uri。


predicates:
  - After= 2019-07-31T23:59:59.789+08:00[Asia/Shanghai]


如果我们将配置的时间设置到当前时之后，浏览器会显示404，此时证明没有路由到配置的uri.


Header Route Predicate Factory

Header Route Predicate Factory需要2个参数，一个是header名，另外一个header值，该值可以是一个正则表达式。当此断言匹配了请求的header名和值时，断言通过，进入到router的规则中去。
在工程的配置文件加上以下的配置：

spring:
  profiles:
    active: header_route
---
spring:
  cloud:
    gateway:
      routes:
      - id: header_route
        uri: http://www.baidu.com
        predicates:
        - Header=X-Request-Id, \d+
  profiles: header_route


在上面的配置中，当请求的Header中有X-Request-Id的header名，且header值为数字时，请求会被路由到配置的 uri. 使用curl执行以下命令:
curl -H X-Request-Id:1 localhost:8081

执行命令后，会正确的返回请求结果,自动访问百度网页。如果在请求中没有带上X-Request-Id的header名，并且值不为数字时，请求就会报404，路由没有被正确转发。

Cookie Route Predicate Factory

Cookie Route Predicate Factory需要2个参数，一个时cookie名字，另一个时值，可以为正则表达式。它用于匹配请求中，带有该名称的cookie和cookie匹配正则表达式的请求。

spring:
  profiles:
    active: cookie_route
---
spring:
  cloud:
    gateway:
      routes:
      - id: cookie_route
        uri: http://www.baidu.com
        predicates:
        - Cookie=name,chengkun
  profiles: cookie_route

在上面的配置中，请求带有cookie名为name, cookie值为chengkun 的请求将都会转发到uri为 http://www.baidu.com的地址上。使用curl执行以下命令:
curl -H Cookie:name=chengkun localhost:8081

使用curl命令进行请求，在请求中带上 cookie，会返回正确的结果，否则，请求报404错误。

Host Route Predicate Factory

Host Route Predicate Factory需要一个参数即hostname，它可以使用. * 等去匹配host。这个参数会匹配请求头中的host的值，一致，则请求正确转发。
在工程的配置文件，加上以下配置：

spring:
  profiles:
    active: host_route
---
spring:
  cloud:
    gateway:
      routes:
      - id: host_route
        uri: http://www.baidu.com
        predicates:
        - Host=**.chengkun.com
  profiles: host_route


在上面的配置中，请求头中含有Host为chengkun.com的请求将会被路由转发转发到配置的uri。 启动工程，执行以下的curl命令，请求会返回正确的请求结果：
curl -H Host:www.chengkun.com localhost:8081


Method Route Predicate Factory

Method Route Predicate Factory 需要一个参数，即请求的类型。比如GET类型的请求都转发到此路由。在工程的配置文件加上以下的配置：

spring:
  profiles:
    active: method_route

---
spring:
  cloud:
    gateway:
      routes:
      - id: method_route
        uri: http://www.baidu.com
        predicates:
        - Method=GET
  profiles: method_route


在上面的配置中，所有的GET类型的请求都会路由转发到配置的uri。使用 curl命令模拟 get类型的请求，会得到正确的返回结果。
curl localhost:8081

使用 curl命令模拟 post请求，则返回404结果。
curl -XPOST localhost:8081


Path Route Predicate Factory

Path Route Predicate Factory 需要一个参数: 一个spel表达式，应用匹配路径。
在工程的配置文件application.yml文件中，做以下的配置：

spring:
  profiles:
    active: path_route
---
spring:
  cloud:
    gateway:
      routes:
      - id: path_route
        uri: http://www.baidu.com
        predicates:
        - Path=/foo/{segment}
  profiles: path_route


在上面的配置中，所有的请求路径满足/foo/{segment}的请求将会匹配并被路由，比如/foo/1 、/foo/bar的请求，将会命中匹配，并成功转发。
使用curl模拟一个请求localhost:8081/foo/dew，执行之后会返回正确的请求结果。
curl localhost:8081/foo/dew


Query Route Predicate Factory

Query Route Predicate Factory 需要2个参数:一个参数名和一个参数值的正则表达式。在工程的配置文件application.yml做以下的配置：

spring:
  profiles:
    active: query_route
---
spring:
  cloud:
    gateway:
      routes:
      - id: query_route
        uri: http://www.baidu.com
        predicates:
        - Query=foo, ba
  profiles: query_route


在上面的配置文件中，配置了请求中含有参数foo，并且foo的值匹配ba.，则请求命中路由，比如一个请求中含有参数名为foo，值的为bar，能够被正确路由转发。
模拟请求的命令如下：
curl localhost:8081?foo=ba


Query Route Predicate Factory也可以只填一个参数，填一个参数时，则只匹配参数名，即请求的参数中含有配置的参数名，则命中路由。比如以下的配置中，配置了请求参数中含有参数名为foo 的参数将会被请求转发到uri为http://www.baidu.com。

十五、spring cloud gateway之filter

 15.1 filter的作用和生命周期
由filter工作流程点，可以知道filter有着非常重要的作用，在“pre”类型的过滤器可以做参数校验、权限校验、流量监控、日志输出、协议转换等，在“post”类型的过滤器中可以做响应内容、响应头的修改，日志的输出，流量监控等。首先需要弄清一点为什么需要网关这一层，这就不得不说下filter的作用了。
作用：
当我们有很多个服务时，比如下图中的user-service、goods-service、sales-service等服务，客户端请求各个服务的Api时，每个服务都需要做相同的事情，比如鉴权、限流、日志输出等。

对于这样重复的工作，有没有办法做的更好，答案是肯定的。在微服务的上一层加一个全局的权限控制、限流、日志输出的Api Gatewat服务，然后再将请求转发到具体的业务服务层。这个Api Gateway服务就是起到一个服务边界的作用，外接的请求访问系统，必须先通过网关层。

生命周期
Spring Cloud Gateway同zuul类似，有“pre”和“post”两种方式的filter。客户端的请求先经过“pre”类型的filter，然后将请求转发到具体的业务服务，比如上图中的user-service，收到业务服务的响应之后，再经过“post”类型的filter处理，最后返回响应到客户端。

与zuul不同的是，filter除了分为“pre”和“post”两种方式的filter外，在Spring Cloud Gateway中，filter从作用范围可分为另外两种，一种是针对于单个路由的gateway filter，它在配置文件中的写法同predict类似；另外一种是针对于所有路由的global gateway filer。现在从作用范围划分的维度来讲解这两种filter。

15.2 gateway filter
GatewayFilter工厂同上一篇介绍的Predicate工厂类似，都是在配置文件application.yml中配置，遵循了约定大于配置的思想，只需要在配置文件配置GatewayFilter Factory的名称，而不需要写全部的类名，比如AddRequestHeaderGatewayFilterFactory只需要在配置文件中写AddRequestHeader，而不是全部类名。在配置文件中配置的GatewayFilter Factory最终都会相应的过滤器工厂类处理。

Spring Cloud Gateway 内置的过滤器工厂一览表如下：


常见的过滤器工厂来讲解，每一个过滤器工厂在官方文档都给出了详细的使用案例，如果不清楚的还可以在org.springframework.cloud.gateway.filter.factory看每一个过滤器工厂的源码。

AddRequestHeader GatewayFilter Factory
创建service-gateway-filter，引入相关的依赖,包括spring boot 版本2.0.5，spring Cloud版本Finchley，gateway依赖如下：
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.chengkun</groupId>
        <artifactId>springcloud</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <groupId>com.chengkun</groupId>
    <artifactId>service-gateway-filter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>service-gateway-filter</name>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
    </dependencies>
</project>


在工程的配置文件中，加入以下的配置：

server:
  port: 8082
spring:
  profiles:
    active: add_request_header_route

---
spring:
  cloud:
    gateway:
      routes:
        - id: add_request_header_route
          uri: http://httpbin.org:80/get
          filters:
            - AddRequestHeader=X-Request-Foo, Bar
          predicates:
            - After= 2019-07-30T23:59:59.789+08:00[Asia/Shanghai]
  profiles: add_request_header_route


在上述的配置中，工程的启动端口为8081，配置文件为add_request_header_route，在add_request_header_route配置中，配置了roter的id为add_request_header_route，路由地址为http://httpbin.org:80/get，该router有AfterPredictFactory，有一个filter为AddRequestHeaderGatewayFilterFactory(约定写成AddRequestHeader)，AddRequestHeader过滤器工厂会在请求头加上一对请求头，名称为X-Request-Foo，值为Bar。为了验证AddRequestHeaderGatewayFilterFactory是怎么样工作的，查看它的源码，AddRequestHeaderGatewayFilterFactory的源码如下：

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cloud.gateway.filter.factory;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory.NameValueConfig;
import org.springframework.http.server.reactive.ServerHttpRequest;

public class AddRequestHeaderGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {
    public AddRequestHeaderGatewayFilterFactory() {
    }

    public GatewayFilter apply(NameValueConfig config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest().mutate().header(config.getName(), config.getValue()).build();
            return chain.filter(exchange.mutate().request(request).build());
        };
    }
}


由上面的代码可知，根据旧的ServerHttpRequest创建新的 ServerHttpRequest ，在新的ServerHttpRequest加了一个请求头，然后创建新的 ServerWebExchange ，提交过滤器链继续过滤。
启动工程，通过curl命令来模拟请求：
curl localhost:8082

最终显示了从http://httpbin.org:80/get得到了请求，响应如下：
{
  "args": {},
  "headers": {
    "Accept": "*/*",
    "Forwarded": "proto=http;host=\"localhost:8082\";for=\"127.0.0.1:59143\"",
    "Host": "httpbin.org",
    "User-Agent": "curl/7.65.3",
    "X-Forwarded-Host": "localhost:8082",
    "X-Request-Foo": "Bar"
  },
  "origin": "127.0.0.1, 36.7.144.234, 127.0.0.1",
  "url": "https://localhost:8082/get"
}



可以上面的响应可知，确实在请求头中加入了X-Request-Foo这样的一个请求头，在配置文件中配置的AddRequestHeader过滤器工厂生效。
跟AddRequestHeader过滤器工厂类似的还有AddResponseHeader过滤器工厂，在此就不再重复。

RewritePath GatewayFilter Factory
在Nginx服务启中有一个非常强大的功能就是重写路径，Spring Cloud Gateway默认也提供了这样的功能，这个功能是Zuul没有的。在配置文件中加上以下的配置：

spring:
  profiles:
    active: rewritepath_route
---
spring:
  cloud:
    gateway:
      routes:
        - id: rewritepath_route
          uri: https://blog.csdn.net
          predicates:
            - Path=/foo/**
          filters:
            - RewritePath=/foo/(?<segment>.*), /$\{segment}
  profiles: rewritepath_route


上面的配置中，所有的/foo/**开始的路径都会命中配置的router，并执行过滤器的逻辑，在本案例中配置了RewritePath过滤器工厂，此工厂将/foo/(?.*)重写为{segment}，然后转发到https://blog.csdn.net。比如在网页上请求localhost:8082/foo/forezp，此时会将请求转发到https://blog.csdn.net/forezp的页面，比如在网页上请求localhost:8082/foo/forezp/1，页面显示404，就是因为不存在https://blog.csdn.net/forezp/1这个页面。

自定义过滤器
Spring Cloud Gateway内置了19种强大的过滤器工厂，能够满足很多场景的需求，那么能不能自定义自己的过滤器呢，当然是可以的。在spring Cloud Gateway中，过滤器需要实现GatewayFilter和Ordered2个接口。写一个RequestTimeFilter，代码如下：
package com.chengkun.cloud.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author:Administrator
 * @date:2019/8/1
 * @description:
 */
public class RequestTimeFilter implements GatewayFilter, Ordered {

    private static final Log log = LogFactory.getLog(GatewayFilter.class);
    private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        exchange.getAttributes().put(REQUEST_TIME_BEGIN,System.currentTimeMillis());
        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    Long startTime = exchange.getAttribute(REQUEST_TIME_BEGIN);
                    if (startTime != null) {
                        log.info(exchange.getRequest().getURI().getRawPath() + ": " + (System.currentTimeMillis() - startTime) + "ms");
                    }
                })
        );
    }

    @Override
    public int getOrder() {
        return 0;
    }
}


在上面的代码中，Ordered中的int getOrder()方法是来给过滤器设定优先级别的，值越大则优先级越低。还有有一个filter(exchange,chain)方法，在该方法中，先记录了请求的开始时间，并保存在ServerWebExchange中，此处是一个“pre”类型的过滤器，然后再chain.filter的内部类中的run()方法中相当于"post"过滤器，在此处打印了请求所消耗的时间。然后将该过滤器注册到router中，代码如下：
package com.chengkun.cloud;

import com.chengkun.cloud.filter.RequestTimeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServiceGatewayFilterApplication {

    @Autowired
    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(ServiceGatewayFilterApplication.class, args);
    }

    @Bean
    public RouteLocatorBuilder routeLocatorBuilder() {
        return new RouteLocatorBuilder(context);
    }

    @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
        // @formatter:off
        return builder.routes()
                .route(r -> r.path("/customer/**")
                        .filters(f -> f.filter(new RequestTimeFilter())
                                .addResponseHeader("X-Response-Default-Foo", "Default-Bar"))
                        .uri("http://httpbin.org:80/get")
                        .order(0)
                        .id("customer_filter_router")
                )
                .build();
        // @formatter:on
    }
}


重启程序，通过curl命令模拟请求：
curl localhost:8082/customer/123

在程序的控制台输出一下的请求信息的日志：
2019-08-01 10:57:58.134  INFO 8836 --- [ctor-http-nio-1] o.s.cloud.gateway.filter.GatewayFilter   : /customer/123: 234ms


15.3 自定义过滤器工厂
在上面的自定义过滤器中，有没有办法自定义过滤器工厂类呢?这样就可以在配置文件中配置过滤器了。现在需要实现一个过滤器工厂，在打印时间的时候，可以设置参数来决定是否打印请参数。查看GatewayFilterFactory的源码，可以发现GatewayFilterFactory的层级如下：

过滤器工厂的顶级接口是GatewayFilterFactory，我们可以直接继承它的两个抽象类来简化开发AbstractGatewayFilterFactory和AbstractNameValueGatewayFilterFactory，这两个抽象类的区别就是前者接收一个参数（像StripPrefix和我们创建的这种），后者接收两个参数（像AddResponseHeader）。

过滤器工厂的顶级接口是GatewayFilterFactory，有2个两个较接近具体实现的抽象类，分别为AbstractGatewayFilterFactory和AbstractNameValueGatewayFilterFactory，这2个类前者接收一个参数，比如它的实现类RedirectToGatewayFilterFactory；后者接收2个参数，比如它的实现类AddRequestHeaderGatewayFilterFactory类。现在需要将请求的日志打印出来，需要使用一个参数，这时可以参照RedirectToGatewayFilterFactory的写法。
package com.chengkun.cloud.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * @author:Administrator
 * @date:2019/8/1
 * @description:
 */
@Component
public class RequestTimeGatewayFilterFactory extends AbstractGatewayFilterFactory<RequestTimeGatewayFilterFactory.Config> {

    private static final Log LOG = LogFactory.getLog(GatewayFilter.class);
    private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";
    private static final String KEY = "withParams";

    public RequestTimeGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(KEY);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange,chain) ->{
            exchange.getAttributes().put(REQUEST_TIME_BEGIN,System.currentTimeMillis());
            return chain.filter(exchange).then(
                    Mono.fromRunnable(() -> {
                        Long startTime = exchange.getAttribute(REQUEST_TIME_BEGIN);
                        if(startTime != null){
                            StringBuilder sb = new StringBuilder(exchange.getRequest().getURI().getRawPath())
                                    .append(": ")
                                    .append(System.currentTimeMillis() - startTime)
                                    .append("ms");
                            if(config.isWithParams()){
                                sb.append(" params:").append(exchange.getRequest().getQueryParams());
                            }
                            LOG.info(sb.toString());
                        }
                    })
            );
        };
    }

    public static class Config{

        private boolean withParams;

        public boolean isWithParams(){
            return withParams;
        }

        public void setWithParams(boolean withParams) {
            this.withParams = withParams;
        }
    }
}


在上面的代码中 apply(Config config)方法内创建了一个GatewayFilter的匿名类，具体的实现逻辑跟之前一样，只不过加了是否打印请求参数的逻辑，而这个逻辑的开关是config.isWithParams()。静态内部类类Config就是为了接收那个boolean类型的参数服务的，里边的变量名可以随意写，但是要重写List shortcutFieldOrder()这个方法。
需要注意的是，在类的构造器中一定要调用下父类的构造器把Config类型传过去，否则会报ClassCastException,最后，需要在工程的启动文件ServiceGatewayFilterApplication类中，向Srping Ioc容器注册RequestTimeGatewayFilterFactory类的Bean。
@Bean
public RequestTimeGatewayFilterFactory myGatewayFilterFactory(){
    return new RequestTimeGatewayFilterFactory();
}


然后可以在配置文件中配置如下：
spring:
  profiles:
    active: elapse_route

---
spring:
  cloud:
    gateway:
      routes:
      - id: elapse_route
        uri: http://httpbin.org:80/get
        filters:
        - RequestTime=false
        predicates:
        - After=2017-01-20T17:42:47.789-07:00[America/Denver]
  profiles: elapse_route


启动工程，在浏览器上访问localhost:8081?name=chengkun，可以在控制台上看到，日志输出了请求消耗的时间和请求参数。

15.4 global filter
Spring Cloud Gateway根据作用范围划分为GatewayFilter和GlobalFilter，二者区别如下：
GatewayFilter : 需要通过spring.cloud.routes.filters 配置在具体路由下，只作用在当前路由上或通过spring.cloud.default-filters配置在全局，作用在所有路由上

GlobalFilter : 全局过滤器，不需要在配置文件中配置，作用在所有的路由上，最终通过GatewayFilterAdapter包装成GatewayFilterChain可识别的过滤器，它为请求业务以及路由的URI转换为真实业务服务的请求地址的核心过滤器，不需要配置，系统初始化时加载，并作用在每个路由上。

Spring Cloud Gateway框架内置的GlobalFilter如下：

上图中每一个GlobalFilter都作用在每一个router上，能够满足大多数的需求。但是如果遇到业务上的定制，可能需要编写满足自己需求的GlobalFilter。在下面的案例中将讲述如何编写自己GlobalFilter，该GlobalFilter会校验请求中是否包含了请求参数“token”，如何不包含请求参数“token”则不转发路由，否则执行正常的逻辑。代码如下：
package com.chengkun.cloud.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author:Administrator
 * @date:2019/8/2
 * @description:
 */
@Component
public class TokenFilter implements GlobalFilter, Ordered {

    private static final Log log = LogFactory.getLog(TokenFilter.class);
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        if(token == null && token.isEmpty()){
            log.info("token is empty...");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}


在上面的TokenFilter需要实现GlobalFilter和Ordered接口，这和实现GatewayFilter很类似。然后根据ServerWebExchange获取ServerHttpRequest，然后根据ServerHttpRequest中是否含有参数token，如果没有则完成请求，终止转发，否则执行正常的逻辑。

然后需要将TokenFilter在工程的启动类中注入到Spring Ioc容器中，代码如下：
@Bean
public TokenFilter tokenFilter(){
        return new TokenFilter();
}


启动工程，使用curl命令请求：
curl localhost:8082/customer/123


可以看到请没有被转发，请求被终止，并在控制台打印了如下日志：
2019-08-02 09:08:38.913  INFO 7936 --- [ctor-http-nio-2] com.chengkun.cloud.filter.TokenFilter    : token is empty...


使用curl命令请求
curl localhost:8082/customer?token=1


控制台打印：
2019-08-02 09:16:11.065  INFO 7936 --- [ctor-http-nio-2] o.s.cloud.gateway.filter.GatewayFilter   : /customer: 491ms



十六、spring cloud gateway 之限流
在高并发的系统中，往往需要在系统中做限流，一方面是为了防止大量的请求使服务器过载，导致服务不可用，另一方面是为了防止网络攻击。

常见的限流方式，比如Hystrix适用线程池隔离，超过线程池的负载，走熔断的逻辑。在一般应用服务器中，比如tomcat容器也是通过限制它的线程数来控制并发的；也有通过时间窗口的平均速度来控制流量。常见的限流纬度有比如通过Ip来限流、通过uri来限流、通过用户访问频次来限流。

一般限流都是在网关这一层做，比如Nginx、Openresty、kong、zuul、Spring Cloud Gateway等；也可以在应用层通过Aop这种方式去做限流。

16.1 常见的限流算法

计数器算法
计数器算法采用计数器实现限流有点简单粗暴，一般我们会限制一秒钟的能够通过的请求数，比如限流qps为100，算法的实现思路就是从第一个请求进来开始计时，在接下去的1s内，每来一个请求，就把计数加1，如果累加的数字达到了100，那么后续的请求就会被全部拒绝。等到1s结束后，把计数恢复成0，重新开始计数。具体的实现可以是这样的：对于每次服务调用，可以通过AtomicLong#incrementAndGet()方法来给计数器加1并返回最新值，通过这个最新值和阈值进行比较。这种实现方式，相信大家都知道有一个弊端：如果我在单位时间1s内的前10ms，已经通过了100个请求，那后面的990ms，只能眼巴巴的把请求拒绝，我们把这种现象称为“突刺现象”。

漏桶算法
漏桶算法为了消除"突刺现象"，可以采用漏桶算法实现限流，漏桶算法这个名字就很形象，算法内部有一个容器，类似生活用到的漏斗，当请求进来时，相当于水倒入漏斗，然后从下端小口慢慢匀速的流出。不管上面流量多大，下面流出的速度始终保持不变。不管服务调用方多么不稳定，通过漏桶算法进行限流，每10毫秒处理一次请求。因为处理的速度是固定的，请求进来的速度是未知的，可能突然进来很多请求，没来得及处理的请求就先放在桶里，既然是个桶，肯定是有容量上限，如果桶满了，那么新进来的请求就丢弃。

在算法实现方面，可以准备一个队列，用来保存请求，另外通过一个线程池（ScheduledExecutorService）来定期从队列中获取请求并执行，可以一次性获取多个并发执行。
这种算法，在使用过后也存在弊端：无法应对短时间的突发流量。

令牌桶算法
从某种意义上讲，令牌桶算法是对漏桶算法的一种改进，桶算法能够限制请求调用的速率，而令牌桶算法能够在限制调用的平均速率的同时还允许一定程度的突发调用。在令牌桶算法中，存在一个桶，用来存放固定数量的令牌。算法中存在一种机制，以一定的速率往桶中放令牌。每次请求调用需要先获取令牌，只有拿到令牌，才有机会继续执行，否则选择选择等待可用的令牌、或者直接拒绝。放令牌这个动作是持续不断的进行，如果桶中令牌数达到上限，就丢弃令牌，所以就存在这种情况，桶中一直有大量的可用令牌，这时进来的请求就可以直接拿到令牌执行，比如设置qps为100，那么限流器初始化完成一秒后，桶中就已经有100个令牌了，这时服务还没完全启动好，等启动完成对外提供服务时，该限流器可以抵挡瞬时的100个请求。所以，只有桶中没有令牌时，请求才会进行等待，最后相当于以一定的速率执行。

实现思路：可以准备一个队列，用来保存令牌，另外通过一个线程池定期生成令牌放到队列中，每来一个请求，就从队列中获取一个令牌，并继续执行。
                           
16.2 Spring Cloud Gateway限流
在Spring Cloud Gateway中，有Filter过滤器，因此可以在“pre”类型的Filter中自行实现上述三种过滤器。但是限流作为网关最基本的功能，Spring Cloud Gateway官方就提供了RequestRateLimiterGatewayFilterFactory这个类，适用Redis和lua脚本实现了令牌桶的方式。具体实现逻辑在RequestRateLimiterGatewayFilterFactory类中，lua脚本在如下图所示的文件夹中：


首先在工程的pom文件中引入gateway的起步依赖和redis的reactive依赖，代码如下：
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.chengkun</groupId>
        <artifactId>springcloud</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <groupId>com.chengkun</groupId>
    <artifactId>service-gateway-limiter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>service-gateway-limiter</name>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
        </dependency>
    </dependencies>
</project>


在配置文件中做以下的配置：

server:
  port: 8083
spring:
  cloud:
    gateway:
      routes:
        - id: limit_route
          uri: http://httpbin.org:80/get
          predicates:
            - After= 2019-07-30T23:59:59.789+08:00[Asia/Shanghai]
          filters:
            - name: RequestRateLimiter
              args:
                key-resolver: '#{@hostAddrKeyResolver}'
                redis-rate-limiter.replenishRate: 1
                redis-rate-limiter.burstCapacity: 3
  application:
    name: gateway-limiter
  redis:
    host: localhost
    port: 6379
    database: 0


在上面的配置文件，指定程序的端口为8081，配置了 redis的信息，并配置了RequestRateLimiter的限流过滤器，该过滤器需要配置三个参数：
burstCapacity：令牌桶总容量。
replenishRate：令牌桶每秒填充平均速率。
key-resolver：用于限流的键的解析器的 Bean 对象的名字。它使用 SpEL 表达式根据#{@beanName}从 Spring 容器中获取 Bean 对象。

KeyResolver需要实现resolve方法，比如根据Hostname进行限流，则需要用hostAddress去判断。
package com.chengkun.cloud.resolver;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author:Administrator
 * @date:2019/8/3
 * @description:
 */
@Component
public class HostAddrKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}


实现完KeyResolver之后，需要将这个类的Bean注册到Ioc容器中。
package com.chengkun.cloud;

import com.chengkun.cloud.resolver.HostAddrKeyResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServiceGatewayLimiterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceGatewayLimiterApplication.class, args);
    }

    @Bean
    public HostAddrKeyResolver hostAddrKeyResolver() {
        return new HostAddrKeyResolver();
    }
}


可以根据uri去限流，这时KeyResolver代码如下：
package com.chengkun.cloud.resolver;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author:Administrator
 * @date:2019/8/3
 * @description:
 */
@Component
public class UrlKeyResolver implements KeyResolver {
    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest().getURI().getPath());
    }
}


在启动类型注入bean
@Bean
public UrlKeyResolver urlKeyResolver(){
    return new UrlKeyResolver();
}


也可以以用户的维度去限流：
@Bean
KeyResolver userKeyResolver() {
    return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("user"));
}


用jmeter进行压测，配置10thread去循环请求lcoalhost:8081，循环间隔1s。从压测的结果上看到有部分请求通过，由部分请求失败。



十七、spring cloud gateway之服务注册与发现
17.1 工程介绍
模块名 端口 作用
eureka-server 8761 注册中心 eureka server
eureka-client 8762 服务提供者 eureka client
service-gateway 8080 路由网关 eureka client

这三个工程中，其中service-client、service-gateway向注册中心eureka-server注册。用户的请求首先经过service-gateway，根据路径由gateway的predict去断言进到哪一个router,router经过各种过滤器处理后，最后路由到具体的业务服务，比如service-client。如图：

在service-gateway模块添加eureka-client的起步依赖，代码如下：
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>


在工程的配置文件application.yml中 ，指定程序的启动端口为8080，注册地址、gateway的配置等信息，配置信息如下：
server:
  port: 8080

spring:
  application:
    name: service-gateway
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
          lowerCaseServiceId: true

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/


其中，spring.cloud.gateway.discovery.locator.enabled为true，表明gateway开启服务注册和发现的功能，并且spring cloud gateway自动根据服务发现为每一个服务创建了一个router，这个router将以服务名开头的请求路径转发到对应的服务。spring.cloud.gateway.discovery.locator.lowerCaseServiceId是将请求路径上的服务名配置为小写（因为服务注册的时候，向注册中心注册时将服务名转成大写的了），比如以/service-hi/*的请求路径被路由转发到服务名为service-hi的服务上。

在浏览器上请求输入localhost:8080/eureka-client/hi?name=1323，网页获取以下的响应：
hi 1323 ,i am from port:8762


在上面的例子中，向gateway-service发送的请求时，url必须带上服务名eureka-client这个前缀，才能转发到eureka-client上，转发之前会将eureka-client去掉。
可以自定义路径并转发到具体的服务上。答案是肯定的是可以的，只需要修改工程的配置文件application.yml，具体配置如下：
server:
  port: 8080

spring:
  application:
    name: service-gateway
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
          lowerCaseServiceId: true
      routes:
        - id: eureka-client
          uri: lb://EUREKA-CLIENT
          predicates:
            - Path=/demo/**
          filters:
            - StripPrefix=1

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/


在上面的配置中，配置了一个Path 的predict,将以/demo/**开头的请求都会转发到uri为lb://EUREKA-CLIENT的地址上，lb://EUREKA-CLIENT即eureka-client服务的负载均衡地址，并用StripPrefix的filter 在转发之前将/demo去掉。同时将spring.cloud.gateway.discovery.locator.enabled改为false，如果不改的话，之前的localhost:8080/eureka-client/hi?name=1323这样的请求地址也能正常访问，因为这时为每个服务创建了2个router。

在浏览器上请求localhost:8080/demo/hi?name=1323，浏览器返回以下的响应：
hi 1323 ,i am from port:8762

