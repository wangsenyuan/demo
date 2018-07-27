package b;

import a.ParentClass;

public class ChildTwo extends ParentClass {
    @Override
    protected void foo() {
        System.out.println("in ChildTwo#foo");
    }

    public void buzz() {
        //        super.buzz();
    }
}
