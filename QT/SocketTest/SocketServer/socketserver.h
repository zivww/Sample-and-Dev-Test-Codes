#ifndef SOCKETSERVER_H
#define SOCKETSERVER_H

#include <QObject>
#include <QDebug>
#include <QTcpServer>
#include <QTcpSocket>
#include <QMainWindow>


namespace Ui {
    class SocketServer;
}


class SocketServer : public QMainWindow
{
    Q_OBJECT

public:
    SocketServer(QWidget *parent = nullptr);
    ~SocketServer();

public slots:
    void onNewConnection();
    void onSocketStateChanged(QAbstractSocket::SocketState socketState);
    void onReadyRead();
private:
    Ui::SocketServer *ui;
    QTcpServer  _server;
    QList<QTcpSocket*>  _sockets;
};
#endif // SOCKETSERVER_H
