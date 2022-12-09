package org.firstinspires.ftc.teamcode;
/**
    Союз нерушимый республик свободных
    Сплотила навеки Великая Русь.
    Да здравствует созданный волей народов
    Единый, могучий Советский Союз!
*/
public class OurMath {
    
    private double epsilon = 0.001;
            
    public double sAbs( double x)
    {
        return Math.sqrt(x*x + epsilon);     
    }
    
    public double sMax( double a, double b)
    {
        return 0.5*( a + b + sAbs(a - b));
    }
    
    public double sMin(double a, double b)
    {
        return 0.5*( a + b - sAbs(a - b));
    }
    
    public double sClamp(double x, double min, double max)
    {
        return sMax(sMin(x,max), min);        
    }
    
    OurMath( double epsilon)
    {
        this.epsilon = epsilon;
    }
}
