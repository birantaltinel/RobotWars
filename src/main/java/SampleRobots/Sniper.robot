double x = getXCoordinate();
double y = getYCoordinate();

if(x == 0 && y == 0) {
    double[] enemyCoordinates = scan();
    if(enemyCoordinates != null) {
        fire(enemyCoordinates[0], enemyCoordinates[1]);
    }
}

else if(x != 0) {
    move(270, 10);
}

else {
    move(180, 10);
}



