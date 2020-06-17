#include "socketclient.h"

#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    SocketClient socketClient;
    socketClient.show();
    return a.exec();
}
