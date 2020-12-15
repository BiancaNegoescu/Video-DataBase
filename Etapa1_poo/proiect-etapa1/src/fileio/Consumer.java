package fileio;

public class Consumer {

    private long id;
    private long initialBudget;
    private long monthlyIncome;

    public Consumer(final long id, final long initialBudget,
                    final long monthlyIncome) {
        this.id = id;
        this.initialBudget = initialBudget;
        this.monthlyIncome = monthlyIncome;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getInitialBudget() {
        return initialBudget;
    }

    public void setInitialBudget(long initialBudget) {
        this.initialBudget = initialBudget;
    }

    public long getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(long monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    @Override
    public String toString() {
        return "ConsumerInputData{" +
                "id='" + id + '\'' +
                ", initialBudget='" + initialBudget + '\'' +
                ", monthlyIncome='" + monthlyIncome + '\'' +
                '}';
    }
}
