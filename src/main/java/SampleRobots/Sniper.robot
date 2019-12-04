double x = getXCoordinate();
double y = getYCoordinate();

if(x == 500 && y == 500) {
    double[] enemyCoordinates = scan();
    if(enemyCoordinates != null) {
        fire(enemyCoordinates[0], enemyCoordinates[1]);
    }
}

else if(y != 500) {
    move(90, 1);
}

else {
    move(0, 1);
}



