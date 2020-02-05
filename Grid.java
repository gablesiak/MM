package project;

import javafx.scene.control.Cell;
import project.alg.FurtherMoore;
import project.alg.GrowthMethod;
import project.alg.Moore;
import project.alg.VonNeumann;
import project.inclu.Inclusion;
import project.inclu.RoundInclusion;
import project.inclu.SquareInclusion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.util.*;
import java.util.List;

public class Grid extends JPanel implements Runnable {

    private static final long serialVersionUID = 1L;

    private int tabSizeX = Core.Config.width / Core.Config.pixelSize;
    private int tabSizeY = Core.Config.height / Core.Config.pixelSize;

    private final GridMap gridMap = new GridMap(this.tabSizeX, this.tabSizeY);
    private final Set<CellColor> restrictedAreas = new Restrictions();

    public Grid() {
        setPreferredSize(new Dimension(500, 500));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                double placeX = (double) e.getX() / getWidth();
                double placeY = (double) e.getY() / getHeight();

                int x = Double.valueOf(gridMap.getW() * placeX).intValue();
                int y = Double.valueOf(gridMap.getH() * placeY).intValue();

                CellColor color = gridMap.find(x, y);
                restrictedAreas.add(color);
                System.out.println("restricted color: " + color + " @ " + "x=" + x + " y=" + y);
            }
        });

    }

    public void run() {

    }

    public void naive() {
        gridMap.initialize();

        GrowthMethod method;
        if (Core.Config.neigh == 1) {
            method = new VonNeumann();
        } else if(Core.Config.neigh == 2) {
            method = new FurtherMoore();
        } else {
            method = new Moore();
        }

        addInclusionBefore();
        growth(method);
    }

    private void growth(GrowthMethod method) {
        GrainGrowth growth = new GrainGrowth(gridMap, method);
        while (growth.iterate(restrictedAreas)) ;
        restrictedAreas.clear();
    }

    public void clean() {
        this.gridMap.clear();
        this.restrictedAreas.clear();
    }

    public void clearGrid() {
        this.gridMap.clear(restrictedAreas);
    }

    @Override
    public void paintComponent(Graphics g) {
        Set<DisplayPoint> boundaries = new HashSet<>();
        if (Core.Config.boundaries) {
            boundaries = boundaries();
        }

        drawFigure(g, boundaries);
    }

    private void drawFigure(Graphics g, Set<DisplayPoint> boundaries) {
        double scaleW = (double) getWidth() / gridMap.getW();
        double scaleH = (double) getHeight() / gridMap.getH();

        for (int x = 0; x < gridMap.getW(); x++) {
            for (int y = 0; y < gridMap.getH(); y++) {
                CellColor me = this.gridMap.find(x, y);

                if (me == null || Core.Config.bleach && me != CellColor.Black()) {
                    me = CellColor.White();
                }

                if (boundaries.contains(new DisplayPoint(x, y))) {
                    me = CellColor.Black();
                }

                g.setColor(new Color(me.R, me.G, me.B));

                g.fillRect(
                        (int) Math.floor(x * scaleW), // position X
                        (int) Math.floor(y * scaleH), // position Y
                        (int) Math.floor(Core.Config.pixelSize * scaleW), // width
                        (int) Math.floor(Core.Config.pixelSize * scaleH) // height
                );
            }
        }
    }

    private Set<DisplayPoint> boundaries() {
        Set<DisplayPoint> boundaries = new HashSet<>(320);

        for (int x = 0; x < gridMap.getW(); x++) {
            for (int y = 0; y < gridMap.getH(); y++) {

                CellColor me = gridMap.find(x, y);

                if (me == null || me == CellColor.Black()) {
                    continue;
                }

                if (!Objects.equals(gridMap.find(x - 1, y), me) && gridMap.find(x - 1, y) != CellColor.Black()) {
                    boundaries.add(new DisplayPoint(x, y));
                } else if (!Objects.equals(gridMap.find(x + 1, y), me) && gridMap.find(x + 1, y) != CellColor.Black()) {
                    boundaries.add(new DisplayPoint(x, y));
                } else if (!Objects.equals(gridMap.find(x, y - 1), me) && gridMap.find(x, y - 1) != CellColor.Black()) {
                    boundaries.add(new DisplayPoint(x, y));
                } else if (!Objects.equals(gridMap.find(x, y + 1), me) && gridMap.find(x, y + 1) != CellColor.Black()) {
                    boundaries.add(new DisplayPoint(x, y));
                }
            }
        }
        return boundaries;
    }

    public String gridAsString() {
        StringBuilder builder = new StringBuilder();

        for (int x = 0; x < gridMap.getW(); x++) {
            for (int y = 0; y < gridMap.getH(); y++) {
                if (gridMap.find(x, y) == null) {
                    continue;
                }
                builder.append(new DisplayPoint(x, y));
                builder.append(" ");
                builder.append(gridMap.find(x, y));
                builder.append("\r\n");

                if ((x * gridMap.getW() + y) % 100 == 0) {
                    System.out.println("Exported: " + x);
                }
            }
        }
        return builder.toString();
    }

    public void readGrid(Map<DisplayPoint, CellColor> readPkts) {
        clean();

        readPkts.forEach((pkt, cellColor) -> {
            this.gridMap.assign(pkt.x, pkt.y, cellColor);
        });
    }

    public void readGrid(BufferedImage read) {
        clean();

        Image image = read.getScaledInstance(tabSizeX, tabSizeY, Image.SCALE_FAST);
        BufferedImage newImage = new BufferedImage(tabSizeX, tabSizeY, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        for (int x = 0; x < newImage.getWidth(); x++) {
            for (int y = 0; y < newImage.getHeight(); y++) {
                CellColor me = createCell(newImage.getRGB(x, y));
                if (Objects.equals(CellColor.Black(), me)) {
                    continue;
                }
                this.gridMap.assign(x, y, me);
            }
        }

        growth(new VonNeumann());
    }

    private CellColor createCell(int clr) {
        Color c = new Color(clr, false);
        return new CellColor(c.getRed(), c.getGreen(), c.getBlue());
    }

    public void readGrid(BufferedInputStream ois) {
        Map<DisplayPoint, CellColor> x = new HashMap<>();
        Scanner scanner = new Scanner(ois);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String prepared_string = line.replaceAll("\\D", " ").replaceAll("\\s+", " ");
            String numbers[] = prepared_string.split(" ");
            DisplayPoint displayPoint = new DisplayPoint(Integer.parseInt(numbers[1]), Integer.parseInt(numbers[2]));
            CellColor cellColor = new CellColor();
            cellColor.R = Integer.parseInt(numbers[3]);
            cellColor.G = Integer.parseInt(numbers[4]);
            cellColor.B = Integer.parseInt(numbers[5]);
            x.put(displayPoint, cellColor);
        }

        readGrid(x);
    }

    private void addInclusionBefore() {
        if (!Core.Config.inclusionBefore) {
            return;
        }

        Inclusion inclusion = inclusionType();

        for (int i = 0; i < Core.Config.amountInclu; i++) {
            inclusion.make(gridMap, gridMap.randomizePkt());
        }
    }

    private Inclusion inclusionType() {
        restrictedAreas.add(CellColor.Black());

        Inclusion inclusion;
        if (Core.Config.typeInclu == 0)
            inclusion = new SquareInclusion();
        else
            inclusion = new RoundInclusion();
        return inclusion;
    }

    public void addInclusionAfter() {
        List<DisplayPoint> boundries = new ArrayList<>(boundaries());
        Random r = new Random();

        Inclusion inclusion = inclusionType();

        for (int i = 0; i < Core.Config.amountInclu; i++) {
            inclusion.make(gridMap, boundries.get(r.nextInt(boundries.size())));
        }
    }
}
