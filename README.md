# ChRL Collection

[![GitHub version](https://badge.fury.io/gh/vinzynth%2FChRL.svg)](http://badge.fury.io/gh/vinzynth%2FChRL)
[![Maven Central](http://maven-badges.herokuapp.com/maven-central/at.chrl/chrl-bom/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22at.chrl%22)
[![Build Status](https://travis-ci.org/vinzynth/ChRL.svg?branch=master)](https://travis-ci.org/vinzynth/ChRL)
[![Coverage Status](https://coveralls.io/repos/vinzynth/ChRL/badge.svg?branch=master&service=github)](https://coveralls.io/github/vinzynth/ChRL?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/55d3c471265ff6001a000d5d/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55d3c471265ff6001a000d5d) 
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

------------------------------------------------------------------------

The ChRL-Collection is a set of various extensions of existing frameworks, making them easier to use due enhanced api wrappers and common used preconfigurations.  
The intention is to reduce the coders work to a minimum and keep his code as simple and short as possible.

-----------------------------------------------------------------------

&nbsp;&nbsp;&nbsp;<sup>**"The expectations of life depend upon diligence; the mechanic that would perfect his work must first sharpen his tools."**<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- *Confucius*</sup>

-----------------------------------------------------------------------

# Packages

## Utils Module    
[![Dependency Status](https://www.versioneye.com/user/projects/55d13d4615ff9b00220001ca/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55d13d4615ff9b00220001ca) - chrl-utils

Common used utils.

### Highlights

* `at.chrl.nutils.ClassUtils`
    * getString, hashCode and equalse template functions.
* `at.chrl.nuitls.DatasetGenerator`
    * Generates instances of given class and filling attributes recursively.
    * Uses Objenesis
* `at.chrl.nutils.Memoizer`
    * Wrapper functions for `Function` and `Predicate` implementations, for quick chaching.
* `at.chrl.nutils.Rnd`
    * Collection of random functions, e.g. get random element of a array.
* `at.chrl.nutils.interfaces.INestedCollection` and `at.chrl.nutils.interfaces.INestedMap` 
    * Instances of classes which implements one of this two interfaces can be used like the nested collection returned by the abstract getter function.


## Config Module
[![Dependency Status](https://www.versioneye.com/user/projects/55d13d4f15ff9b001c000189/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55d13d4f15ff9b001c000189) - chrl-config  

Powerfull configuration framework.

### Highlights

* The `Property` annotation is used by the framework to load and write from a .properties file.
* Type detection via reflection API.
* Properties can used directly as object instance.

```java
@Property(key = "nio.threads.read", defaultValue = "0")
public static int NIO_READ_THREADS;
```

* **Supported configuration types:**
    * Boolean
    * Byte
    * Character
    * Double
    * Float
    * Integer
    * Long
    * Short
    * String
    * Enums
    * Arrays
    * **java.util.function.Function**
    * **java.util.function.BiFunction**
    * File
    * Date
    * java.util.regex.Patter
    * Class
    * PrintStreams
    * InetSocketAddress

## Cron Module
[![Dependency Status](https://www.versioneye.com/user/projects/55d13d4f15ff9b00140001b5/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55d13d4f15ff9b00140001b5) - chrl-cron  

Cron API which is based on the Quartz library.

### Usage

```java
CronService.getInstance().schedule(() -> {
    System.out.println("tick");
}, "*/2 * * * * ?");
```

## ORM Module
[![Dependency Status](https://www.versioneye.com/user/projects/55d13d4b15ff9b001c000177/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55d13d4b15ff9b001c000177) - chrl-orm  

## Additional Modules

[![Dependency Status](https://www.versioneye.com/user/projects/55d13d4515ff9b001c000155/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55d13d4515ff9b001c000155) - chrl-parent  
[![Dependency Status](https://www.versioneye.com/user/projects/55d13d4715ff9b001c00015a/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55d13d4715ff9b001c00015a) - chrl-spring-sample  
[![Dependency Status](https://www.versioneye.com/user/projects/55d13d4715ff9b0014000189/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55d13d4715ff9b0014000189) - chrl-spring  
[![Dependency Status](https://www.versioneye.com/user/projects/55d13d4815ff9b00220001d2/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55d13d4815ff9b00220001d2) - chrl-orm-test  
[![Dependency Status](https://www.versioneye.com/user/projects/55d13d4b15ff9b00140001a4/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55d13d4b15ff9b00140001a4) - chrl-rebellion  
[![Dependency Status](https://www.versioneye.com/user/projects/55d13d4c15ff9b00220001e6/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55d13d4c15ff9b00220001e6) - chrl-database  
[![Dependency Status](https://www.versioneye.com/user/projects/55d13d4c15ff9b00140001a6/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55d13d4c15ff9b00140001a6) - chrl-orm-spring-integration  
[![Dependency Status](https://www.versioneye.com/user/projects/55d13d4d15ff9b00220001ec/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55d13d4d15ff9b00220001ec) - chrl-callbacks  
[![Dependency Status](https://www.versioneye.com/user/projects/55d13d4e15ff9b001c000187/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55d13d4e15ff9b001c000187) - chrl-iryna  
[![Dependency Status](https://www.versioneye.com/user/projects/55d13d5015ff9b00140001bb/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55d13d5015ff9b00140001bb) - chrl-callback-utils