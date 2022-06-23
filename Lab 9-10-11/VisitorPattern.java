import java.util.ArrayList;

public class VisitorPattern {
    public static void main(String[] args) {
        ArrayList<Company> companies = new ArrayList<>();
        companies.add(new HomeImprovement("home improvement", 400000.0, 20));
        companies.add(new Electronics("electronics", 800000.0, 75));
        companies.add(new Logistics("logistics", 650000.0, 50));
        companies.add(new MobilePhone("mobile phone", 850000.0, 80));

        for (Company company : companies) {
            company.Accept(new TaxVisitor());
            company.Accept(new IncomePerBranchVisitor());
            company.Accept(new TotalTaxVisitor());
        }
    }
}

interface Element {
    void Accept(Visitor visitor);
}

interface Visitor {
    void Visit(HomeImprovement element);
    void Visit(Electronics element);
    void Visit(Logistics element);
    void Visit(MobilePhone element);
}

abstract class Company implements Element {
    protected String name;
    protected int noOfBranches;
    protected double yearlyIncome, tax;

    public Company(String name, double yearlyIncome, int noOfBranches) {
        setName(name);
        setYearlyIncome(yearlyIncome);
        setNoOfBranches(noOfBranches);
        setTax(0.0);
    }

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    public void setNoOfBranches(int noOfBranches) {
        int minNoOfBranches = 1, maxNoOfBranches = 100;

        if (noOfBranches < minNoOfBranches)
            noOfBranches = minNoOfBranches;
        else if (noOfBranches > maxNoOfBranches)
            noOfBranches = maxNoOfBranches;

        this.noOfBranches = noOfBranches;
    }
    public int getNoOfBranches() { return noOfBranches; }

    public void setYearlyIncome(double yearlyIncome) {
        double minYearlyIncome = 100000.0, maxYearlyIncome = 900000.0;

        if (yearlyIncome < minYearlyIncome)
            yearlyIncome = minYearlyIncome;
        else if (yearlyIncome > maxYearlyIncome)
            yearlyIncome = maxYearlyIncome;

        this.yearlyIncome = yearlyIncome;
    }
    public double getYearlyIncome() { return yearlyIncome; }

    public void setTax(double tax) { this.tax = tax; }
    public double getTax() { return tax; }
}

class HomeImprovement extends Company {
    public HomeImprovement(String name, double yearlyIncome, int noOfBranches) { super(name, yearlyIncome, noOfBranches); }

    public void Accept(Visitor visitor) { visitor.Visit(this); }
}

class Electronics extends Company {
    public Electronics(String name, double yearlyIncome, int noOfBranches) { super(name, yearlyIncome, noOfBranches); }

    public void Accept(Visitor visitor) { visitor.Visit(this); }
}

class Logistics extends Company {
    public Logistics(String name, double yearlyIncome, int noOfBranches) { super(name, yearlyIncome, noOfBranches); }

    public void Accept(Visitor visitor) { visitor.Visit(this); }
}

class MobilePhone extends Company {
    public MobilePhone(String name, double yearlyIncome, int noOfBranches) { super(name, yearlyIncome, noOfBranches); }

    public void Accept(Visitor visitor) { visitor.Visit(this); }
}

class TaxVisitor implements Visitor {
    public void Visit(HomeImprovement element) {
        element.setTax(element.getYearlyIncome() * 0.18);
        System.out.println(element.getName() + "'s tax due: " + element.getTax());
    }

    public void Visit(Electronics element) {
        element.setTax(element.getYearlyIncome() * 0.36);
        System.out.println(element.getName() + "'s tax due: " + element.getTax());
    }

    public void Visit(Logistics element) {
        element.setTax(element.getYearlyIncome() * 0.08);
        System.out.println(element.getName() + "'s tax due: " + element.getTax());
    }

    public void Visit(MobilePhone element) {
        element.setTax(element.getYearlyIncome() * 0.42);
        System.out.println(element.getName() + "'s tax due: " + element.getTax());
    }
}

class IncomePerBranchVisitor implements Visitor {
    public void Visit(HomeImprovement element) {
        double yearlyNetIncome = element.getYearlyIncome() - ((element.getYearlyIncome() * 0.20) + element.getTax());
        double averageNetIncomePerBranch = yearlyNetIncome / element.getNoOfBranches();
        System.out.println(element.getName() + "'s average net income per branch: " + averageNetIncomePerBranch);
    }

    public void Visit(Electronics element) {
        double yearlyNetIncome = element.getYearlyIncome() - ((element.getYearlyIncome() * 0.10) + element.getTax());
        double averageNetIncomePerBranch = yearlyNetIncome / element.getNoOfBranches();
        System.out.println(element.getName() + "'s average net income per branch: " + averageNetIncomePerBranch);
    }

    public void Visit(Logistics element) {
        double yearlyNetIncome = element.getYearlyIncome() - ((element.getYearlyIncome() * 0.25) + element.getTax());
        double averageNetIncomePerBranch = yearlyNetIncome / element.getNoOfBranches();
        System.out.println(element.getName() + "'s average net income per branch: " + averageNetIncomePerBranch);
    }

    public void Visit(MobilePhone element) {
        double yearlyNetIncome = element.getYearlyIncome() - ((element.getYearlyIncome() * 0.22) + element.getTax());
        double averageNetIncomePerBranch = yearlyNetIncome / element.getNoOfBranches();
        System.out.println(element.getName() + "'s average net income per branch: " + averageNetIncomePerBranch);
    }
}

class TotalTaxVisitor implements Visitor {
    public void Visit(HomeImprovement element) {

    }

    public void Visit(Electronics element) {

    }

    public void Visit(Logistics element) {

    }

    public void Visit(MobilePhone element) {

    }
}
