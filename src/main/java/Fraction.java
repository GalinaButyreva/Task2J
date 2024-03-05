interface  Fractionable{
    double doubleValue();
    void setNum(int num);
    void setDenum(int denum);
    void setNumForTest(int num); // добавлено для проверки, чтобы сравнить с желаемым результатом  в CacheTest
    public double multiplyValue();
}
public class Fraction implements Fractionable {
    private  int num;
    private  int denum;
    public Fraction(int num, int denum) {
        this.num = num;
        this.denum = denum;
    }

   @Cache
   @Override
    public double doubleValue() {
        return (double) num/denum;
    }

    @Mutator
    @Override
    public void setNum(int num) {
        this.num = num;
    }

    @Mutator
    @Override
    public void setDenum(int denum) {
        this.denum = denum;
    }

    @Cache
    @Override
    public double multiplyValue() {
        return (double) num*denum;
    }


    @Override
    public void setNumForTest(int num) {
        this.num = num;
    }

}
