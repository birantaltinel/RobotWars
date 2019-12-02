double x = getXCoordinate();
double y = getYCoordinate();

if(x == 0 && y == 0) {
    int scanDegree = 0;
    while(scanDegree <= 90) {
        double distance = scan(scanDegree, scanDegree);
        if(distance != -1) {
            fire(scanDegree, distance);
            return;
        }
        scanDegree += 1;
    }
}

else if(x != 0) {
    move(270, 10);
}

else {
    move(180, 10);
}



