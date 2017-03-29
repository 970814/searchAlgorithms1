package others2017321.main;


import others2017321.algorithms.Lagrange;
import others2017321.algorithms.PointGroup;
import others2017321.algorithms.PolylineComparison;
import others2017321.constant.StaticConstant;
import others2017321.io.Read;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import static others2017321.algorithms.PolylineComparison.sResembleParts;

/**
 * Created by L on 2017/3/21.
 */

public class Graphics {
    public static BasicStroke fatStroke = new BasicStroke(2.0f);
    public static BasicStroke thinStroke = new BasicStroke(1.0f);
    private static BasicStroke fatterStroke = new BasicStroke(3.0f);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JFrame() {


            private Object lock = new Object();
            Color water = new Color(55, 168, 226);
            public boolean drawLagrange = false;
            public boolean drawBarycentricLagrange = false;
            JFrame this0 = this;
            ArrayList<PointGroup> groups = new ArrayList<>();
            int current = -1;
            final JComponent mainComponent;
            ////////////////////////////////////////
            int r = 8;
            //////////////////////////////////////
            boolean addMode = false;
            boolean removeMode = false;
            boolean normalMode = true;
            boolean selectMode = false;
            boolean movedMode = false;
            /////////////////////////////////////////
            JPopupMenu popupMenu = new JPopupMenu();
            String[] str = {"show number", "show location", "show linkLines", "show length"};
            String[] str2 = {"normal", "add", "remove", "select", "moveTo"};
            JCheckBoxMenuItem[] items = new JCheckBoxMenuItem[str.length];
            JRadioButtonMenuItem[] radioItems = new JRadioButtonMenuItem[str2.length];

            ///////////////////////////////////////
            private boolean paintPoint = false;
            private boolean paintLocation = false;
            private boolean paintLines = false;
            private boolean paintLength = false;
            public boolean paintSlope = false;
            public boolean paintMainSlope = false;
            ///////////////////////////////////////
            Point point = new Point(0, 0);
            Point start = null;
            Stack<Point2D.Float> movePoints = new Stack<>();
            Double dist = null;
            ///////////////////////////////////////////
            public boolean runFlag = false;
            Rectangle rectangle = null;
            boolean spaceFlag = true;
            Rectangle2D.Float[] keyListenerRectangle = new Rectangle2D.Float[]{null};
            Rectangle2D.Float[] similarPart = new Rectangle2D.Float[]{null};
            //////////////////////////////////////////


//--------------------------------------------------------------------------------
            {
                setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                setSize(StaticConstant.Width, StaticConstant.Height);
                setLocationRelativeTo(null);
                sResembleParts = new ArrayList<>();
            }

            {
                for (int i = 0; i < items.length; i++) {
                    items[i] = new JCheckBoxMenuItem(str[i]);
                    popupMenu.add(items[i]);
                }
                items[0].addActionListener((l) -> {
                    paintPoint = !paintPoint;
                    this0.repaint();
                });
                items[1].addActionListener((l) -> {
                    paintLocation = !paintLocation;
                    this0.repaint();
                });
                items[2].addActionListener((l) -> {
                    paintLines = !paintLines;
                    this0.repaint();
                });
                items[3].addActionListener((l) -> {
                    paintLength = !paintLength;
                    this0.repaint();
                });
                ButtonGroup group = new ButtonGroup();
                for (int i = 0; i < radioItems.length; i++) {
                    radioItems[i] = new JRadioButtonMenuItem(str2[i]);
                    popupMenu.add(radioItems[i]);
                    group.add(radioItems[i]);
                }
                radioItems[0].setSelected(true);
                radioItems[0].addActionListener((l) -> {
                    normalMode = true;
                    addMode = false;
                    removeMode = false;
                    selectMode = false;
                    movedMode = false;
                    movePoints.clear();
                    PolylineComparison.clear();
                });
                radioItems[1].addActionListener((l) -> {
                    if (current < 0) {
                        if (groups.isEmpty()) groups.add(new PointGroup());
                        current = 0;
                    }
                    normalMode = false;
                    addMode = true;
                    removeMode = false;
                    selectMode = false;
                    movedMode = false;
                    movePoints.clear();
                    PolylineComparison.clear();
                });
                radioItems[2].addActionListener((l) -> {
                    normalMode = false;
                    addMode = false;
                    removeMode = true;
                    movedMode = false;
                    selectMode = false;
                    movePoints.clear();
                    PolylineComparison.clear();
                });
                radioItems[3].addActionListener((l) -> {
                    normalMode = false;
                    addMode = false;
                    removeMode = false;
                    selectMode = true;
                    movedMode = false;
                });
                radioItems[4].addActionListener((l) -> {
                    normalMode = false;
                    addMode = false;
                    removeMode = false;
                    selectMode = false;
                    movedMode = true;
                });
            }

            {

                setJMenuBar(new JMenuBar(){
                    {
                        add(new JMenu("set bounds"){
                            {

                                setMnemonic('S');
                                add(new JMenuItem("set c1"){
                                    {
                                        addActionListener(e -> StaticConstant.c1 = Double.parseDouble(JOptionPane.showInputDialog(null, "current c2: " + StaticConstant.c1)));
                                    }
                                });
                                add(new JMenuItem("set flag"){
                                    {
                                        setAccelerator(KeyStroke.getKeyStroke("ctrl F"));
                                        addActionListener(e -> StaticConstant.flag = Boolean.parseBoolean(JOptionPane.showInputDialog(null, "current c2: " + StaticConstant.flag)));
                                    }
                                });
                                add(new JMenuItem("set c2"){
                                    {
                                        addActionListener(e -> StaticConstant.c2 = Double.parseDouble(JOptionPane.showInputDialog(null, "current c2: " + StaticConstant.c2)));
                                    }
                                });
                                add(new JMenuItem("set c"){
                                    {
                                        addActionListener(e -> StaticConstant.c = Double.parseDouble(JOptionPane.showInputDialog(null, "current c2: " + StaticConstant.c)));
                                    }
                                });


                            }
                        });
                    }
                });
            }

            {
                addKeyListener(new KeyAdapter() {

                    @Override
                    public void keyPressed(KeyEvent e) {
                        char keyChar = e.getKeyChar();
                        switch (keyChar) {
                            case 32://space:
                                if (spaceFlag && !groups.isEmpty()) {
                                    spaceFlag = false;

                                    if (groups.size() == 0) {
                                        groups.add(new PointGroup());
                                        current = 0;
                                        radioItems[1].doClick();
                                        break;
                                    } else if (groups.size() > 1) {
                                        if (groups.get(current).nPoints == 0) {
                                            Iterator<PolylineComparison.ResemblePart> each = sResembleParts.iterator();
                                            Object o = groups.remove(current);
                                            while (each.hasNext()) {
                                                if (each.next().group == o) {
                                                    each.remove();
                                                    break;
                                                }
                                            }
                                            if (current == groups.size()) current--;
                                        }
                                        else current = (current + 1) % groups.size();
                                    }
                                    drawRectangle(groups.get(current));
                                }
                                break;
                        }
                        this0.repaint();
                    }


                    @Override
                    public void keyReleased(KeyEvent e) {
                        char keyChar = e.getKeyChar();

                        switch (keyChar) {
                            case '/':
                                if (!groups.isEmpty()) {
                                    float maxX = Integer.MIN_VALUE;
                                    float maxY = Integer.MIN_VALUE;
                                    for (PointGroup group : groups) {
//                                        if (group.nPoints > maxX) maxX = group.nPoints;
                                        for (Float xPoint : group.xPoints) if (xPoint > maxX) maxX = xPoint;
                                        for (Float yPoint : group.yPoints) if (yPoint > maxY) maxY = yPoint;
                                    }
                                    StaticConstant.xd = (StaticConstant.Width - 100) / maxX;
                                    StaticConstant.yd = (StaticConstant.Height - 100) / maxY;
                                }
                                break;
                            case 'K':
                                paintMainSlope = !paintMainSlope;
                                break;
                            case 'k':
                                paintSlope = !paintSlope;
                                break;
                            case 'l'://Lagrange
                                drawLagrange = !drawLagrange;
                                break;
                            case 'b':
                                drawBarycentricLagrange = !drawBarycentricLagrange;
                                break;
                            case 32://space:
                                if (!groups.isEmpty()) {
                                    keyListenerRectangle[0] = null;
                                    spaceFlag = true;
                                }
                                break;

                            case 'n':
                                if (current >= 0 && groups.get(current).nPoints == 0) return;
                                groups.add(new PointGroup());
                                current = groups.size() - 1;
                                radioItems[1].doClick();
                                break;
//                            case 's':
//                                int which = Integer.parseInt(
//                                        JOptionPane.showInputDialog(null, "which group you want to operate?"));
//                                if (which >= groups.size()) {
//                                    groups.add(new PointGroup());
//                                    which = groups.size() - 1;
//                                } else if (current >= 0 && groups.get(current).nPoints == 0) return;
//                                current = which;
//                                break;
                            case 'r':
                                if (current >= 0) groups.get(current).cal();
                                break;
                            case 'R':
                                if (groups.get(current).isCaled) groups.get(current).isCaled = false;
                                break;
                            case 'f':
                                if (current >= 0) groups.get(current).calFeature();
                                break;
                            case 'F':
                                if (groups.get(current).isCaledFeature) groups.get(current).isCaledFeature = false;
                                break;
                            case 'c':
                                if (current >= 0) {
                                    PointGroup target  = groups.get(current);
                                    PolylineComparison.clear();
                                    for (PointGroup group : groups) {
                                        if (target != group) {
                                            PolylineComparison.search(target, group);
                                        }
                                    }
                                    System.out.println("like:" + sResembleParts.size());
                                    System.out.println(sResembleParts);
                                    System.out.println(!sResembleParts.isEmpty());//如果不为空，则说明存在相似
                                }
                                break;
                            case 'C':
                                PolylineComparison.clear();
                                break;
                            case 's':
                                String[] str = JOptionPane.showInputDialog(null, "fileId").split(" ");
                                String filename = StaticConstant.parent +
                                        str[0] + ".txt";
                                try {
                                    groups.add(Read.read(filename, Integer.valueOf(str[1]), Integer.valueOf(str[2]), Float.valueOf(str[3]), Float.valueOf(str[4])));
                                    current = groups.size() - 1;
                                } catch (FileNotFoundException e1) {
                                    e1.printStackTrace();
                                }
                                break;
                            case 'A':
                                filename = StaticConstant.parent +
                                        JOptionPane.showInputDialog(null, "fileId") + ".txt";
                                try {
                                    Read.read(filename, groups);
                                    current = groups.size() - 1;
                                } catch (FileNotFoundException e1) {
                                    e1.printStackTrace();
                                }
                                break;
                            case 'a':
                                filename = StaticConstant.parent +
                                        JOptionPane.showInputDialog(null, "fileId") + ".txt";
                                try {

                                    groups.add(Read.read(filename));
                                    current = groups.size() - 1;
                                } catch (FileNotFoundException e1) {
                                    e1.printStackTrace();
                                }
                                break;
                            case 'p':
                            case 27: //esc:
                                return;
                        }
                        this0.repaint();
                    }
                });
                setContentPane(mainComponent = new JComponent() {
                    {
                        addMouseListener(new MouseAdapter() {
                            @Override
                            public void mousePressed(MouseEvent e) {
                                if ((removeMode || selectMode || movedMode) && e.getButton() == MouseEvent.BUTTON1)
                                    start = e.getPoint();
                                else start = null;

                            }
                            public void mouseReleased(MouseEvent event) {
                                if (event.isPopupTrigger()) {
                                    popupMenu.show(event.getComponent(), event.getX(), event.getY());
                                } else if (normalMode) {
                                    return;
                                } else if (addMode) {
                                    groups.get(current).add(
                                            new Point2D.Float(event.getX() / StaticConstant.xd, event.getY() / StaticConstant.yd));
                                    dist = null;
                                    this0.repaint();
                                } else if (removeMode) {
                                    if (runFlag) {
                                        rectangle = null;
                                        this0.repaint();
                                        return;
                                    }
                                    if (rectangle == null) return;
                                    if (current == -1) return;
                                    Point location = rectangle.getLocation();
                                    Dimension size = rectangle.getSize();
                                    PointGroup group = groups.get(current);
                                    for (int i = 0; i < group.nPoints; i++) {
                                        Point2D.Float p = group.get(i);
                                        if (withinBounds(p, location.x, size.width + location.x, location.y, size.height + location.y)) {
                                            group.remove(i);
                                            i--;
                                        }
                                    }
                                    rectangle = null;
                                } else if (selectMode) {
                                    if (runFlag) {
                                        rectangle = null;
                                        this0.repaint();
                                        return;
                                    }
                                    if (rectangle == null) {
                                        return;
                                    }
                                    Point location = rectangle.getLocation();
                                    Dimension size = rectangle.getSize();
                                    if (current >= 0) {
                                        PointGroup group = groups.get(current);
                                        for (int i = 0; i < group.nPoints; i++) {
                                            Point2D.Float p = group.get(i);
                                            if (withinBounds(p, location.x, size.width + location.x, location.y, size.height + location.y))
                                                if (!movePoints.contains(p)) {
                                                    movePoints.add(p);
//                                                    System.out.println(p.toString());
                                                }
                                        }
                                    }
                                    rectangle = null;
                                }
                                this0.repaint();
                            }
                        });
                        addMouseMotionListener(new MouseAdapter() {
                            @Override
                            public void mouseMoved(MouseEvent e) {
                                point = e.getPoint();
                                this0.repaint();
                            }
                            @Override
                            public void mouseDragged(MouseEvent e) {
                                point = e.getPoint();
                                if ((removeMode || selectMode) && start != null) {
                                    Point end = e.getPoint();
                                    rectangle = new Rectangle(Math.min(start.x, end.x), Math.min(start.y, end.y), Math.abs(end.x - start.x), Math.abs(end.y - start.y));
                                    this0.repaint();
                                } else if (movedMode && start != null) {
                                    Point end = e.getPoint();
                                    int x = end.x - start.x;
                                    int y = end.y - start.y;
                                    start = end;
                                    PointGroup group = groups.get(current);
                                    for (Point2D.Float p : movePoints) {
                                        int where = group.search(p);
                                        if (where != -1){
                                            p.x += x / StaticConstant.xd;
                                            p.y += y / StaticConstant.yd;
                                            group.remove(where);
                                            group.add(p);
                                        }
                                    }
                                    this0.repaint();
                                }
                            }
                        });
                    }
                    Color color = new Color(0, 0, 255, 48);
                    @Override
                    protected void paintComponent(java.awt.Graphics g) {
                        Graphics2D d = (Graphics2D) g;


                        d.setColor(color);
                        X.setLine(point.x, 0, point.x, getHeight());
                        Y.setLine(0, point.y, getWidth(), point.y);
                        d.draw(X);
                        d.draw(Y);
                        if (!groups.isEmpty() && current != -1 && groups.get(current).nPoints > 0) {
                            int index = groups.get(current).searchCeilX(point.x / StaticConstant.xd);
                            if (index >= 0) {
                                if (index < groups.get(current).nPoints - 1) {
                                    float x = groups.get(current).xPoints.get(index + 1);
                                    float y = groups.get(current).yPoints.get(index + 1);
                                    line2D.setLine(point.x, point.y
                                            , x * StaticConstant.xd, y * StaticConstant.yd);
                                    d.draw(line2D);
                                    if (paintMainSlope)
                                        paintSlope(point.x, point.y
                                                , x * StaticConstant.xd, y * StaticConstant.yd, d);
                                }
                                float x = groups.get(current).xPoints.get(index);
                                float y = groups.get(current).yPoints.get(index);
                                line2D.setLine(x * StaticConstant.xd, y * StaticConstant.yd
                                        , point.x, point.y);
                            } else {
                                float x = groups.get(current).xPoints.get(0);
                                float y = groups.get(current).yPoints.get(0);
                                line2D.setLine(point.x, point.y, x * StaticConstant.xd, y * StaticConstant.yd);
                            }
                            d.draw(line2D);
                            if (paintMainSlope)
                                paintSlope((float) line2D.getX1(), (float) line2D.getY1()
                                        , (float) line2D.getX2(), (float) line2D.getY2(), d);
                        }
                        d.setColor(Color.BLUE);
                        d.setColor(Color.BLACK);
                        paintLines(d);
                        paintPoint(d);
                        paintLocation(d);
                        paintLength(d);
                        paintSlope(d);
                        if (rectangle != null) {
                            if (removeMode) d.setColor(Color.BLUE);
                            else d.setColor(Color.BLACK);
                            d.draw(rectangle);
                        }

                        d.setColor(Color.YELLOW.darker());
                        d.setStroke(fatStroke);
                        for (Point2D.Float p : movePoints) {
                            line2D.setLine(p.x * StaticConstant.xd, p.y * StaticConstant.yd
                                    , p.x * StaticConstant.xd, p.y * StaticConstant.yd);
                            d.draw(line2D);
                        }
//                        d.setColor(water);
                        paintLinearRegressionEquation(d);
                        if (!spaceFlag && keyListenerRectangle[0] != null) {
                            d.setColor(Color.BLUE);
                            d.draw(keyListenerRectangle[0]);
                        }
                        paintLinearRegressionEquationFeature(d);
                        drawSimilarPart(d);
                        drawLagrange(d);
//                        drawBarycentricLagrange(d);
                        if (!sResembleParts.isEmpty()) {
                            d.setColor(Color.RED);
                            float x = groups.get(current).xPoints.get(0) * StaticConstant.xd;
                            float y = groups.get(current).yPoints.get(0) * StaticConstant.yd;

                            d.drawString("共找到" + sResembleParts.size() + "处相似", x, y);
                        }
                    }
                });
            }



            private void paintSlope(Graphics2D d) {
                if (paintSlope) {
                    d.setColor(Color.BLACK);
                    for (PointGroup group : groups) {
                        paintSlope(d, group);
                    }
                }
            }

            private void paintSlope(Graphics2D d, PointGroup group) {
                for (int i = 0; i < group.nPoints - 1; i++) {
                    int j = i + 1;
                    float x1 = group.xPoints.get(i);
                    float y1 = group.yPoints.get(i);
                    float x2 = group.xPoints.get(j);
                    float y2 = group.yPoints.get(j);
                    paintSlopeK(x1, y1, x2, y2, d);
                }
            }

            private void paintSlopeK(float x1, float y1, float x2, float y2, Graphics2D d) {
                double degrees = Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
                d.drawString(String.format("%.1f", degrees)
                        , ((x1 + x2) / 2) * StaticConstant.xd, ((y2 + y1) / 2) * StaticConstant.yd);
            }
            private void paintSlope(float x1, float y1, float x2, float y2, Graphics2D d) {
                double degrees = Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
                d.drawString(String.format("%.1f", degrees)
                        , ((x1 + x2) / 2), ((y2 + y1) / 2));
            }

            Line2D X = new Line2D.Double();
            Line2D Y = new Line2D.Double();

            private void drawRectangle(PointGroup group) {
                drawRectangle(group, 0, group.nPoints, keyListenerRectangle);
            }
            public void drawSimilarPart(Graphics2D d) {
                d.setStroke(new BasicStroke(1.0f));
                d.setColor(Color.RED);

                for (PolylineComparison.ResemblePart resemblePart : sResembleParts) {
                    drawRectangle(resemblePart.group, resemblePart.mostLike[0], resemblePart.mostLike[1], similarPart);
                    d.draw(similarPart[0]);
                }
            }

            public void drawRectangle(PointGroup group, int from, int to, Rectangle2D.Float[] similarPart) {
                if (to - from < 2) return;
                float minX = group.xPoints.get(from);
                float maxX = group.xPoints.get(to - 1);
                float minY = Integer.MAX_VALUE;
                float maxY = Integer.MIN_VALUE;
                for (int i = from; i < to; i++) {
                    float y = group.yPoints.get(i);
                    if (y > maxY) maxY = y;
                    if (y < minY) minY = y;
                }
                similarPart[0] = new Rectangle2D.Float(minX * StaticConstant.xd, minY * StaticConstant.yd
                        , (maxX - minX) * StaticConstant.xd, (maxY - minY) * StaticConstant.yd);
            }




            Line2D line2D = new Line2D.Float();

            private void paintLinearRegressionEquation(Graphics2D d) {
                d.setColor(Color.ORANGE);
                d.setStroke(Graphics.fatStroke);
                groups.stream().filter(group -> group.isCaled).forEach(group -> {
                    line2D.setLine(group.equation[2], group.equation[3], group.equation[4], group.equation[5]);
                    d.draw(line2D);
                });
            }

            private void paintLinearRegressionEquationFeature(Graphics2D d) {
                d.setColor(Color.ORANGE);
                d.setStroke(Graphics.thinStroke);
                groups.stream().filter(group -> group.isCaledFeature).forEach(group ->
                        group.regressionFeature.equations.forEach(equation -> {
                            line2D.setLine(equation[2]* StaticConstant.xd, equation[3]* StaticConstant.yd
                                    , equation[4]*StaticConstant.xd, equation[5]*StaticConstant.yd);
                            d.draw(line2D);
                        })
                );
            }

            private void paintLength(Graphics2D d) {
                if (!paintLength) return;
                for (PointGroup group : groups)
                    for (int i = 0; i < group.nPoints - 1; i++) {
                        int j = i + 1;
                        paintOneLength(d, i, j, group);
                    }
            }


            public void paintOneLength(Graphics2D d, int i, int j, PointGroup group) {
                Point2D.Float p = group.get(i);
                Point2D.Float q = group.get(j);
                int mX = (int) ((p.x + q.x) * StaticConstant.xd) >>> 1;
                int mY = (int) ((p.y + q.y) * StaticConstant.yd) >>> 1;
                d.translate(mX, mY);
                double theta = Math.atan((p.y - q.y) / (double) (p.x - q.x));
                d.rotate(theta);
                d.drawString(String.format("%.1f", distanceOf(p, q)) + " px", 0, 0);
                d.rotate(-theta);
                d.translate(-mX, -mY);
            }


            private void paintLocation(Graphics2D d) {
                if (!paintLocation) return;
                for (int i = groups.size() - 1; i >= 0; i--) paintLocation(d, groups.get(i));
            }

            private void paintLocation(Graphics2D d, PointGroup group) {
                for (int i = 0; i < group.nPoints; i++) {
                    d.setColor(Color.YELLOW.darker());
                    float x = group.xPoints.get(i);
                    float y = group.yPoints.get(i);
                    d.drawString("(" + x + ", " + y + ")", x * StaticConstant.xd, (y + r) * StaticConstant.yd);
                }
            }

            private void paintLines(Graphics2D d) {
                if (!paintLines) return;
                for (int i = groups.size() - 1; i >= 0; i--) {
                    if (i == current) d.setColor(Color.BLUE);
                    else d.setColor(Color.BLACK);
                    paintLines(d, groups.get(i));
                }
            }

            private void paintLines(Graphics2D d, PointGroup group) {
                if (group.xPoints.size() < 2) return;
                float preX = group.xPoints.get(0);
                float preY = group.yPoints.get(0);
                for (int i = 1; i < group.xPoints.size(); i++) {
                    float x = group.xPoints.get(i);
                    float y = group.yPoints.get(i);
                    line2D.setLine(preX * StaticConstant.xd, preY * StaticConstant.yd
                            , x * StaticConstant.xd, y * StaticConstant.yd);
                    d.draw(line2D);
                    preX = x;
                    preY = y;
                }
            }

            private void paintPoint(Graphics2D d) {
                if (!paintPoint) return;
                for (PointGroup group : groups) paintPoint(d, group);
            }

            public void paintPoint(Graphics2D d, PointGroup group) {
                for (int i = 0; i < group.nPoints; i++) {
                    d.setColor(Color.YELLOW.darker());
                    float x = group.xPoints.get(i);
                    float y = group.yPoints.get(i);
                    d.drawString(String.valueOf(i), x * StaticConstant.xd, (y - r / 2) * StaticConstant.yd);
//                    d.drawString(String.valueOf(i), x , (y - r / 2) );
                }
            }
            private void drawBarycentricLagrange(Graphics2D d) {
                d.setColor(Color.YELLOW);
                if (drawBarycentricLagrange)
                    for (PointGroup group : groups) Lagrange.drawBarycentricLagrangeInterpolationFormula(d, group);
            }
            private void drawLagrange(Graphics2D d) {
                d.setColor(Color.RED);
                if (drawLagrange)
                    for (PointGroup group : groups) Lagrange.drawLagrangeInterpolationFormula(d, group);
            }

            private boolean withinBounds(Point2D.Float p, int x1, int x2, int y1, int y2) {
                return (x1 <= p.x * StaticConstant.xd && p.x * StaticConstant.xd <= x2)
                        && (y1 <= p.y * StaticConstant.yd && p.y * StaticConstant.yd <= y2);
            }
        }.setVisible(true));

    }




    public static double distanceOf(Point2D.Float a, Point2D.Float b) { //计算两点之间的距离
        return Point.distance(a.x, a.y, b.x, b.y);
    }

}
