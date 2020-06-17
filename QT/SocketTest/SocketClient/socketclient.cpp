#include "socketclient.h"
#include "ui_socketclient.h"

#include <QDebug>
#include <QHostAddress>

SocketClient::SocketClient(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::SocketClient)
    , _socket(this)
{
    ui->setupUi(this);
    _socket.connectToHost(QHostAddress("127.0.0.1"), 4242);
    connect(&_socket, SIGNAL(readyRead()), this, SLOT(onReadyRead()));
}

SocketClient::~SocketClient()
{
    delete ui;
}

void SocketClient::onReadyRead()
{
    QByteArray datas = _socket.readAll();
    qDebug() << datas;
    _socket.write(QByteArray("ok !\n"));
}
