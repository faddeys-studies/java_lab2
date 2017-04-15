import java.util.*;
import java.util.function.Function;


public class RectDetector {

    public static void main(String argv[]) {
        Scanner input = new Scanner(System.in);

        final int fieldW, fieldH, rectTop, rectLeft, rectHeight, rectWidth;

        System.out.print("Field size (width, height): ");
        fieldW = readInt(input, (Integer i) -> i > 0, "Width: expected positive integer");
        fieldH = readInt(input, (Integer i) -> i > 0, "Height: expected positive integer");

        System.out.print("Rectangle (top, left, width, height): ");
        rectTop = readInt(input, (Integer i) -> 0 <= i && i <= fieldH,
                "Top: expected number between 0 and "+fieldH);
        rectLeft = readInt(input, (Integer i) -> 0 <= i && i <= fieldW,
                "Left: expected number between 0 and "+fieldW);
        rectHeight = readInt(input, (Integer i) -> 0 <= i && i <= fieldH-rectTop,
                "Height: expected number between 0 and "+(fieldH-rectTop));
        rectWidth = readInt(input, (Integer i) -> 0 <= i && i <= fieldW-rectLeft,
                "Width: expected number between 0 and "+(fieldW-rectLeft));


        char image[][] = new char[fieldH][fieldW];
        for (int i = 0; i < rectTop; i++) {
            Arrays.fill(image[i], '0');
        }
        for (int i = rectTop; i < rectTop+rectHeight; i++) {
            Arrays.fill(image[i], 0, rectLeft, '0');
            Arrays.fill(image[i], rectLeft, rectLeft+rectWidth, '1');
            Arrays.fill(image[i], rectLeft+rectWidth, fieldW, '0');
        }
        for (int i = rectTop+rectHeight; i < fieldH; i++) {
            Arrays.fill(image[i], '0');
        }

        System.out.println("Generated image:");
        for (int i = 0; i < fieldH; i++) {
            System.out.println(new String(image[i]));
        }

        int[] pointWithin = findPointWithinRect(image);
        if (pointWithin == null) {
            System.out.println("No filled points found");
            return;
        }
        System.out.println(
                "Found point within rectangle: " +
                "x=" + pointWithin[1] + ", y=" + pointWithin[0]
        );

        int[] corners = findCorners(image, pointWithin);
        System.out.println("top-left: x=" + corners[1] + ", y=" + corners[0]);
        System.out.println("top-right: x=" + corners[3] + ", y=" + corners[0]);
        System.out.println("bottom-right: x=" + corners[3] + ", y=" + corners[2]);
        System.out.println("bottom-left: x=" + corners[1] + ", y=" + corners[2]);
        System.out.println("center: x=" + (corners[1]+corners[3])/2
                               + ", y=" + (corners[0]+corners[2])/2);

    }


    public static int[] findCorners(char image[][], int[] pointWithin) {
        final int top, left, right, bottom;
        final int W = image[0].length, H = image.length;

        int y = pointWithin[0], x = pointWithin[1];
        while(y>0 && x>0 && image[y-1][x-1]=='1') {
            x--; y--;
        }
        while(y>0 && image[y-1][x]=='1') {
            y--;
        }
        while(x>0 && image[y][x-1]=='1') {
            x--;
        }
        top = y;
        left = x;

        y = pointWithin[0]; x = pointWithin[1];
        while(y<H-1 && x<W-1 && image[y+1][x+1]=='1') {
            x++; y++;
        }
        while(y<H-1 && image[y+1][x]=='1') {
            y++;
        }
        while(x<W-1 && image[y][x+1]=='1') {
            x++;
        }
        bottom = y;
        right = x;

        return new int[] {top, left, bottom, right};
    }

    public static int[] findPointWithinRect(char image[][]) {
        Queue<Rect> searchAreasQueue = new LinkedList<>();
        searchAreasQueue.add(new Rect(0, 0, image[0].length, image.length));
        while (!searchAreasQueue.isEmpty()) {
            Rect rect = searchAreasQueue.poll();
            int cX = rect.centerX(), cY = rect.centerY();
            if (image[cY][cX] == '1') {
                return new int[] {cY, cX};
            }
            if (rect.area() == 1) continue;
            int halfW = rect.w / 2, halfH = rect.h / 2;
            if (halfW != 0 && halfH != 0)
                searchAreasQueue.add(new Rect(rect.x, rect.y, halfW, halfH));
            if (halfW != rect.w && halfH != 0)
                searchAreasQueue.add(new Rect(rect.x+halfW, rect.y, rect.w-halfW, halfH));
            if (halfW != 0 && halfH != rect.h)
                searchAreasQueue.add(new Rect(rect.x, rect.y+halfH, halfW, rect.h-halfH));
            if (halfW != rect.w && halfH != rect.h)
                searchAreasQueue.add(new Rect(rect.x+halfW, rect.y+halfH, rect.w-halfW, rect.h-halfH));
        }
        return null;
    }

    static class Rect {
        public int x, y;
        public int w, h;

        public Rect(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        public int centerX() { return x + w/2; }
        public int centerY() { return y + h/2; }

        public int area() { return w*h; }

        public String toString() { return "("+y+", "+x+"; "+h+", "+w+")"; }
    }


    private static int readInt(Scanner scanner,
                               Function<Integer, Boolean> checker,
                               String message) {
        while (true) {
            int candidate;
            try {
                candidate = scanner.nextInt();
                if (checker == null || checker.apply(candidate)) {
                    return candidate;
                }
            } catch (NoSuchElementException nse) {}
            System.out.println(message);
        }
    }

}
