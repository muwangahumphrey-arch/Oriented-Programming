package vu.geometricshapes;
// Custom Exception - Unchecked (RuntimeException)
class InvalidShapeException extends RuntimeException {
    public InvalidShapeException(String message) {
        super(message);
    }
    
    public InvalidShapeException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Abstract Shape Class
public abstract class Shape {
    protected String color = "white";
    protected boolean filled;
    
    
    public Shape() {
        this.color = "white";
        this.filled = false;
    }
    
    public Shape(String color, boolean filled) {
        this.color = color;
        this.filled = filled;
    }
    
    public abstract double getArea();
    public abstract double getPerimeter();
    public abstract void resize(double factor); // scales the dimensions
    
    @Override
    public String toString() {
        return String.format("Shape[color=%s, filled=%b, area=%.2f, perimeter=%.2f]", 
                            color, filled, getArea(), getPerimeter());
    }
}

// Circle Subclass
class Circle extends Shape {
    private double radius;
    
    public Circle() {
        this(1.0);
    }
    
    public Circle(double radius) {
        this(radius, "white", false);
    }
    
    public Circle(double radius, String color, boolean filled) {
        super(color, filled);
        if (radius <= 0) {
            throw new InvalidShapeException("Radius must be positive: " + radius);
        }
        this.radius = radius;
    }
    
    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
    
    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }
    
    @Override
    public void resize(double factor) {
        if (factor <= 0) {
            throw new InvalidShapeException("Resize factor must be positive: " + factor);
        }
        radius *= factor;
    }
    
    public double getRadius() {
        return radius;
    }
}

// Rectangle Subclass
class Rectangle extends Shape {
    private double width;
    private double height;
    
    public Rectangle() {
        this(1.0, 1.0);
    }
    
    public Rectangle(double width, double height) {
        this(width, height, "white", false);
    }
    
    public Rectangle(double width, double height, String color, boolean filled) {
        super(color, filled);
        if (width <= 0 || height <= 0) {
            throw new InvalidShapeException("Width and height must be positive: width=" 
                                           + width + ", height=" + height);
        }
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double getArea() {
        return width * height;
    }
    
    @Override
    public double getPerimeter() {
        return 2 * (width + height);
    }
    
    @Override
    public void resize(double factor) {
        if (factor <= 0) {
            throw new InvalidShapeException("Resize factor must be positive: " + factor);
        }
        width *= factor;
        height *= factor;
    }
    
    public double getWidth() {
        return width;
    }
    
    public double getHeight() {
        return height;
    }
}

// Triangle Subclass
class Triangle extends Shape {
    private double side1;
    private double side2;
    private double side3;
    
    public Triangle() {
        this(1.0, 1.0, 1.0);
    }
    
    public Triangle(double side1, double side2, double side3) {
        this(side1, side2, side3, "white", false);
    }
    
    public Triangle(double side1, double side2, double side3, String color, boolean filled) {
        super(color, filled);
        validateTriangle(side1, side2, side3);
        this.side1 = side1;
        this.side2 = side2;
        this.side3 = side3;
    }
    
    private void validateTriangle(double s1, double s2, double s3) {
        if (s1 <= 0 || s2 <= 0 || s3 <= 0) {
            throw new InvalidShapeException("All sides must be positive: " 
                                           + s1 + ", " + s2 + ", " + s3);
        }
        if (s1 + s2 <= s3 || s1 + s3 <= s2 || s2 + s3 <= s1) {
            throw new InvalidShapeException("Triangle inequality violated: " 
                                           + s1 + ", " + s2 + ", " + s3);
        }
    }
    
    @Override
    public double getArea() {
        // Heron's formula
        double s = getPerimeter() / 2;
        return Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));
    }
    
    @Override
    public double getPerimeter() {
        return side1 + side2 + side3;
    }
    
    @Override
    public void resize(double factor) {
        if (factor <= 0) {
            throw new InvalidShapeException("Resize factor must be positive: " + factor);
        }
        side1 *= factor;
        side2 *= factor;
        side3 *= factor;
    }
    
    public double getSide1() { return side1; }
    public double getSide2() { return side2; }
    public double getSide3() { return side3; }
}
//(c)Defining Invalid Shape Exception and throwing it from relevant constructors
// ShapeDriver Class with Methods
public class ShapeDriver{
    // Method to print areas using superclass reference (dynamic binding)
    public static void printAreas(Shape[] shapes) {
        System.out.println("Areas of all shapes:");
        for (Shape shape : shapes) {
            System.out.printf("  %s area: %.2f%n", 
                             shape.getClass().getSimpleName(), shape.getArea());
        }
    }
    
    // Method to find shape with largest area
    public static Shape largest(Shape[] shapes) {
        if (shapes == null || shapes.length == 0) {
            return null;
        }
        
        Shape largestShape = shapes[0];
        for (int i = 1; i < shapes.length; i++) {
            if (shapes[i].getArea() > largestShape.getArea()) {
                largestShape = shapes[i];
            }
        }
        return largestShape;
    }
    
    // Main method for testing
    public static void main(String[] args) {
        // Create an array of shapes
        Shape[] shapes = new Shape[4];
        shapes[0] = new Circle(5.0, "Red", true);
        shapes[1] = new Rectangle(4.0, 6.0, "Blue", false);
        shapes[2] = new Triangle(3.0, 4.0, 5.0, "Green", true);
        shapes[3] = new Circle(3.0, "Yellow", true);
        
        // Print all areas
        printAreas(shapes);
        
        // Find and display the largest shape
        Shape largestShape = largest(shapes);
        if (largestShape != null) {
            System.out.printf("%nLargest shape: %s with area: %.2f%n", 
                             largestShape.getClass().getSimpleName(), 
                             largestShape.getArea());
        }
        
        // Demonstrate resizing
        System.out.println("\n--- Resizing Circle ---");
        Circle circle = (Circle) shapes[0];
        System.out.println("Before resize - Radius: " + circle.getRadius());
        circle.resize(2.0);
        System.out.println("After resize (factor 2.0) - Radius: " + circle.getRadius());
        System.out.println("New area: " + circle.getArea());
        
        // Demonstrate exception handling
        System.out.println("\n--- Exception Handling Demonstration ---");
        try {
            Circle invalidCircle = new Circle(-5.0);
        } catch (InvalidShapeException e) {
            System.out.println("Caught exception: " + e.getMessage());
        }
        
        try {
            Triangle invalidTriangle = new Triangle(1.0, 1.0, 3.0);
        } catch (InvalidShapeException e) {
            System.out.println("Caught exception: " + e.getMessage());
        }
    }
}