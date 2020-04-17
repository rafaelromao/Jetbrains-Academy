class TestDrive {
    public static void main(String[] args) throws InterruptedException {
        Laptop laptop;

        LaptopFactory dellFactory = new DellXPSFactory();
        LaptopFactory mackbookFactory = new MackBookFactory();

        System.out.println("-Hello, I need Windows laptop");
        System.out.println("-Okay! Please wait for a sec, - Calling to the DellXPSFactory. -Bring me the Dell XPS 9370");
        Thread.sleep(1500);

        laptop = dellFactory.createComputer();
        System.out.println(laptop.getDescription());
        System.out.println("There it is!\n");

        System.out.println("-Hello, I need MacOS laptop");
        System.out.println("-Okay! Please wait for a sec, - Calling to the MackBookFactory. -Bring me the MackBook Pro 13\"");
        Thread.sleep(1500);

        laptop = mackbookFactory.createComputer();
        System.out.println(laptop.getDescription());
        System.out.println("There it is!");
    }
}

interface LaptopFactory {
    Laptop createComputer();
}

class MackBookFactory implements LaptopFactory {
    @Override
    public Laptop createComputer() {
        LaptopDetailsFactory detailsFactory = new MackBookDetailsFactory();

        return new MackBook(detailsFactory);
    }
}

class DellXPSFactory implements LaptopFactory {
    @Override
    public Laptop createComputer() {
        LaptopDetailsFactory detailsFactory = new DellXPSDetailsFactory();

        return new DellXPS(detailsFactory);
    }
}

interface LaptopDetailsFactory {
    Display createDisplay();
    GraphicCard createGraphicCard();
    Processor createProcessor();
    SSD createSSD();
}

class MackBookDetailsFactory implements LaptopDetailsFactory {
    @Override
    public Display createDisplay() {
        return new MackBookDisplay();
    }

    @Override
    public GraphicCard createGraphicCard() {
        return new MackBookGraphicCard();
    }

    @Override
    public Processor createProcessor() {
        return new MackBookProcessor();
    }

    @Override
    public SSD createSSD() {
        return new MackBookSSD();
    }
}

class DellXPSDetailsFactory implements LaptopDetailsFactory {
    @Override
    public Display createDisplay() {
        return new DellXPSDisplay();
    }

    @Override
    public GraphicCard createGraphicCard() {
        return new DellXPSGraphicCard();
    }

    @Override
    public Processor createProcessor() {
        return new DellXPSProcessor();
    }

    @Override
    public SSD createSSD() {
        return new DellXPSSSD();
    }
}

abstract class Laptop {
    Display display;
    GraphicCard graphicCard;
    Processor processor;
    SSD ssd;

    abstract String getDescription();

    @Override
    public String toString() {
        return "Display: " + display.toString() + "\n"
                + "GraphicCard: " + graphicCard.toString() + "\n"
                + "Processor: " + processor.toString() + "\n"
                + "SSD: " + ssd.toString();
    }
}

class MackBook extends Laptop {

    MackBook(LaptopDetailsFactory detailsFactory) {
        display = detailsFactory.createDisplay();
        graphicCard = detailsFactory.createGraphicCard();
        processor = detailsFactory.createProcessor();
        ssd = detailsFactory.createSSD();
    }

    @Override
    public String getDescription() {
        return "This is THE MackBook Pro 13\"\n" + super.toString();
    }
}

class DellXPS extends Laptop {

    DellXPS(LaptopDetailsFactory detailsFactory) {
        display = detailsFactory.createDisplay();
        graphicCard = detailsFactory.createGraphicCard();
        processor = detailsFactory.createProcessor();
        ssd = detailsFactory.createSSD();
    }

    @Override
    public String getDescription() {
        return "This is Dell XPS 9370\n" + super.toString();
    }
}

interface Display {
    String toString();
}

class MackBookDisplay implements Display {

    @Override
    public String toString() {
        return "13\" 4K display";
    }
}

class DellXPSDisplay implements Display {
    @Override
    public String toString() {
        return "13\" FHD screen";
    }
}

interface GraphicCard {
    String toString();
}

class MackBookGraphicCard implements GraphicCard {
    @Override
    public String toString() {
        return "Intel Iris Plus Graphics 640";
    }
}

class DellXPSGraphicCard implements GraphicCard {
    @Override
    public String toString() {
        return "Intel UHD 620 GPU";
    }
}

interface Processor {
    String toString();
}

class MackBookProcessor implements Processor {

    @Override
    public String toString() {
        return "Dual-Core i5";
    }
}

class DellXPSProcessor implements Processor {
    @Override
    public String toString() {
        return "Core i7";
    }
}

interface SSD {
    String toString();
}

class MackBookSSD implements SSD {
    @Override
    public String toString() {
        return "256Gb SSD";
    }
}

class DellXPSSSD implements SSD {
    @Override
    public String toString() {
        return "256Gb SSD";
    }
}