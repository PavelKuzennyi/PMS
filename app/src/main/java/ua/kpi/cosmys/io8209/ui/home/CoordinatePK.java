package ua.kpi.cosmys.io8209.ui.home;

public class CoordinatePK {

    int degrees;
    int minutes;
    int seconds;
    final Direction ourDir;
    String cardinalPoint;

    public CoordinatePK(){
        ourDir = Direction.LATITUDE;
        cardinalPoint = "N";
        degrees = minutes = seconds = 0;
    }

    public CoordinatePK(int degree, int minute, int second, Direction dir) {
        ourDir = dir;
        if (dir == Direction.LONGITUDE) {
            if (degree >= -180 && degree <= 180) {
                degrees = degree;
                if (degree >= 0)
                    cardinalPoint = "E";
                else
                    cardinalPoint = "W";
            } else {
                System.out.println("Wrong degree!");
            }
        } else {
            if (degree >= -90 && degree <= 90) {
                degrees = degree;
                if (degree >= 0)
                    cardinalPoint = "N";
                else
                    cardinalPoint = "S";
            } else {
                System.out.println("Wrong degree!");
            }
        }

        if (minute >= 0 && minute <= 59) {
            minutes = minute;
        } else {
            System.out.println("Wrong minute!");
        }

        if (second >= 0 && second <= 59) {
            seconds = second;
        } else {
            System.out.println("Wrong second!");
        }
    }


    public Direction getDirection(){
        return ourDir;
    }

    public int getDegree(){
        return degrees;
    }


    public int getMinute(){
        return minutes;
    }


    public int getSecond(){
        return seconds;
    }

    public String getCoordinateA(){
        return String.format("%d° %d' %d\" %s", Math.abs(degrees), minutes, seconds, cardinalPoint);
    }

    public String getCoordinateB(){
        float grad = (Math.abs(degrees) + (float)minutes/60 + (float)seconds/3600)*(degrees >= 0? 1: -1);
        return String.format("%f° %s", Math.abs(grad), cardinalPoint);
    }

    public CoordinatePK getMeanCoordinate(CoordinatePK a, CoordinatePK b){
        if (a.getDirection() == b.getDirection()){
            return new CoordinatePK(
                    (a.getDegree() + b.getDegree()) / 2,
                    (a.getMinute() + b.getMinute()) / 2,
                    (a.getSecond() + b.getSecond()) / 2, a.getDirection());
        }
        else{
            return null;
        }
    }

    public CoordinatePK getMeanCoordinate(CoordinatePK meaning){
        return getMeanCoordinate(this, meaning);
    }
}

enum Direction{
        LATITUDE,
        LONGITUDE
    }