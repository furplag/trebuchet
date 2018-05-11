# trebuchet

[![Build Status](https://travis-ci.org/furplag/trebuchet.svg?branch=master)](https://travis-ci.org/furplag/trebuchet)
[![Coverage Status](https://coveralls.io/repos/github/furplag/trebuchet/badge.svg?branch=master)](https://coveralls.io/github/furplag/trebuchet?branch=master)

code snippets for some problems when handling Exceptions in using Stream API .

## Getting Start

Add the following snippet to any project's pom that depends on your project
```pom.xml
<repositories>
  ...
  <repository>
    <id>savage-reflections</id>
    <url>https://raw.github.com/furplag/trebuchet/mvn-repo/</url>
    <snapshots>
      <enabled>true</enabled>
      <updatePolicy>always</updatePolicy>
    </snapshots>
  </repository>
</repositories>
...
<dependencies>
  ...
  <dependency>
    <groupId>jp.furplag.sandbox</groupId>
    <artifactId>trebuchet</artifactId>
    <version>1.0.0</version>
  </dependency>
</dependencies>
```

## Usage
```java

  public static void main(String[] args) {
    // @formatter:off

    // [1, 2, 3, 4, 5, 6, 7, 8, 9]
    Integer[] vars = IntStream.rangeClosed(1, 9).boxed().toArray(Integer[]::new);
    System.out.println(Arrays.toString(vars));

    // [0, 0, 0, 0, 0, 0, 0, 0, 0]
    System.out.println(Arrays.toString(
      Arrays.stream(vars).map((x) -> (x - 1) / x).toArray(Integer[]::new)
    ));

    try {
      // java.lang.ArithmeticException: zero divide ...
      System.out.println(Arrays.toString(
        Arrays.stream(vars).map((x) -> x / (x - 1)).toArray(Integer[]::new)
      ));
    } catch (Exception e) {/* so, what we have to do next ? */}

    // we decided to try to against Exception, the code was ugliness .
    // [null, 2, 1, 1, 1, 1, 1, 1, 1]
    System.out.println(Arrays.toString(
      IntStream.rangeClosed(1, 9).boxed().map((x) -> {
        try {
          return x / (x - 1);
        } catch (Exception e) {/* so, what we have to do next ? */}

        return null;
      }).toArray(Integer[]::new)
    ));
    // @formatter:on

    // we have to do like that which checking vars every time in those many,  many of code ?
    System.out.println(Arrays.toString(
      Arrays.stream(vars).map((x) -> x == 1 ? null : (x / (x - 1))).toArray(Integer[]::new)
    ));

    // no, not need. That's it.
    System.out.println(Arrays.toString(
        Arrays.stream(vars).map(Trebuchet.orElse((x) -> x / (x - 1), (ex, x) -> null)).toArray(Integer[]::new)
    ));
  }
```

## License
Code is under the [Apache Licence v2](LICENCE).
