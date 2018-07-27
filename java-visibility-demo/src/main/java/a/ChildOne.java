package a;

public class ChildOne extends ParentClass {
    @Override
    protected void foo() {
        System.out.println("in ChildOne#foo");
    }

    @Override
    public void bar() {
        System.out.println("in ChildOne#bar");
    }
/*
    @Override
    private void buzz() {
        super.buzz();
    }*/

    @Override
    public void buzz() {
        System.out.println("in ChildOne:buzz");
    }
}
