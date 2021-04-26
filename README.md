# trebuchet
[![deprecated](https://img.shields.io/badge/deprecated-integrated%20as%20a%20part%20of%20Relic-red.svg)](https://github.com/furplag/relic)
[![Build Status](https://travis-ci.org/furplag/trebuchet.svg?branch=master)](https://travis-ci.org/furplag/trebuchet)
[![Coverage Status](https://coveralls.io/repos/github/furplag/trebuchet/badge.svg?branch=master)](https://coveralls.io/github/furplag/trebuchet?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/02fcf40271c746be8adbb6d3df04b52e)](https://www.codacy.com/app/furplag/trebuchet?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=furplag/trebuchet&amp;utm_campaign=Badge_Grade)
[![codebeat badge](https://codebeat.co/badges/42cc12ce-7ae8-489c-bfa4-4d655159f029)](https://codebeat.co/projects/github-com-furplag-trebuchet-master)
[![Maintainability](https://api.codeclimate.com/v1/badges/c572835a3dffc65a2517/maintainability)](https://codeclimate.com/github/furplag/trebuchet/maintainability)

code snippets for some problems when handling java.lang.Throwables in using Stream API .

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
    <version>[3.0,)</version>
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
      Arrays.stream(vars).map(ThrowableFunction.of((x) -> x / (x - 1), (x, ex) -> null)).toArray(Integer[]::new)
  ));
  System.out.println(Arrays.toString(
      Arrays.stream(vars).map((t) -> ThrowableFunction.orElse(t, (x) -> x / (x - 1), (x, ex) -> null)).toArray(Integer[]::new)
  ));
  System.out.println(Arrays.toString(
      Arrays.stream(vars).map((i) -> ThrowableFunction.orNull(i, (x) -> x / (x - 1))).toArray(Integer[]::new)
  ));
}
```

## License
Code is under the [Apache Licence v2](LICENCE).
