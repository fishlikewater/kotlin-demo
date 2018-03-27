package fishlikewater.kotlindemo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StreamTest {

    public void testLimitAndSkip() {
        List<Person> persons = new ArrayList();
        for (int i = 1; i <= 10000; i++) {
            Person person = new Person(i, "name" + i);
            persons.add(person);
        }
        List<String> personList2 = persons.stream().
                map(Person::getName).limit(10).skip(3).collect(Collectors.toList());
        System.out.println(personList2);
    }

    public static void main(String[] args) {
        StreamTest streamTest = new StreamTest();
        streamTest.testLimitAndSkip();
    }

}


class Person {
    public int no;
    private String name;

    public Person(int no, String name) {
        this.no = no;
        this.name = name;
    }

    public String getName() {
        System.out.println(name);
        return name;
    }
}