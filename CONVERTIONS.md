## 코드 규약

이 가이드는 코드 작성 시 일관성 및 가독성을 높이기 위한 기본적인 규칙을 제시합니다.

**1. 소스 파일 구조**

* **UTF-8 인코딩**: 모든 소스 파일은 UTF-8 인코딩을 사용합니다.
* **패키지 선언**: 소스 파일은 패키지 선언으로 시작하며, 소문자로 작성합니다. 예: `package com.example.project;`
* **임포트 선언**: 필요한 클래스는 패키지 선언 후 임포트합니다. 와일드카드(*) 임포트는 사용하지 않는 것을 권장합니다.
* **클래스 선언**: 각 소스 파일에는 하나의 최상위 클래스만 포함하는 것이 좋습니다.

**2. 들여쓰기**

* **탭 대신 공백**: 들여쓰기에는 탭 대신 4개의 공백을 사용합니다.
* **줄 바꿈**: 연산자와 피연산자 사이, 메서드 파라미터 사이에는 공백을 추가합니다.
* **중괄호**: 여는 중괄호는 같은 줄의 끝에 위치하고, 닫는 중괄호는 새 줄에 위치합니다. 닫는 중괄호는 여는 중괄호와 같은 들여쓰기를 유지합니다.

```java
public class Example {

    public void exampleMethod(int parameter1, int parameter2) {
        // ...
    }
}
```

**3. 명명 규칙**

* **클래스명**: 각 단어의 첫 글자를 대문자로 작성하는 PascalCase를 사용합니다. 예: `MyClass`
* **메서드명**: 동사 또는 동사구로 시작하고, 첫 글자는 소문자로, 이후 단어의 첫 글자는 대문자로 작성하는 camelCase를 사용합니다. 예: `calculateSum()`
* **변수명**: 첫 글자는 소문자로, 이후 단어의 첫 글자는 대문자로 작성하는 camelCase를 사용합니다. 예: `myVariable`
* **상수명**: 모든 글자를 대문자로 작성하고, 단어 사이는 밑줄(_)로 구분합니다. 예: `MAX_VALUE`

**4. 주석**

* **Javadoc**: 모든 공개 클래스, 인터페이스, 메서드 및 필드에 Javadoc 주석을 사용합니다.
* **블록 주석**: 여러 줄에 걸쳐 설명이 필요한 경우 블록 주석을 사용합니다.
* **한 줄 주석**: 간단한 설명은 한 줄 주석을 사용합니다.

```java
/**
 * 블록 주석은 이렇게 작성하세요.
 */
public class Example {

    // 한 줄 주석은 이렇게 작성하세요.
    public void exampleMethod(/* DO NOT THIS */) {
        // ...
    }
    
    /**
     * Javadoc 주석은 이렇게 작성하세요.
     * 
     * @param parameter1 설명
     * @param parameter2 설명
     * @return 설명
     */
    public int exampleMethod(int parameter1, int parameter2) {
        // ...
    }
}
```

**5. 기타**

* **코드 라인 길이**: 가독성을 위해 코드 라인 길이는 80~120자 이내로 유지합니다.
* **공백**: 연산자, 메서드 파라미터, 쉼표 뒤에는 공백을 추가합니다.
* **중복 코드**: 중복 코드를 최소화하고, 재사용 가능한 코드를 작성합니다.

```java
public class Example {

    public void exampleMethod(int parameter1, int parameter2) {
        int sum = parameter1 + parameter2;
        System.out.println("Sum: " + sum);
    }
}
```

**참고 자료**

* [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
* [Oracle Code Conventions for the Java Programming Language](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html)

**커밋 규칙**

* **커밋 메시지**: 커밋 메시지는 영어로 작성하며, 제목과 본문은 빈 줄로 구분합니다.
* **커밋 타입**: 커밋 타입은 ADD, UPDATE, FIX, REMOVE 등을 사용합니다.
* **커밋 제목**: 제목은 50자 이내로 작성하며, 첫 글자는 대문자로 작성합니다.
* **커밋 본문**: 본문은 변경 사유와 방법을 상세히 설명하며, 72자 이내로 작성합니다.

```
ADD : exampleMethod in Example class
- Add exampleMethod to calculate sum of two numbers
- Add exampleMethod to print sum of two numbers
```
```
UPDATE : exampleMethod in Example class
- Update exampleMethod to calculate sum of two numbers
- Update exampleMethod to print sum of two numbers
```
```
FIX : exampleMethod in Example class
- Fix exampleMethod to calculate sum of two numbers
- Fix exampleMethod to print sum of two numbers
```

