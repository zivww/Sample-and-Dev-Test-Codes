#include "socketserver.h"
#include <QApplication>

int main(int argc, char *argv[]){
    QApplication a(argc, argv);
    SocketServer socketServer;
    socketServer.show();
    return a.exec();
}
