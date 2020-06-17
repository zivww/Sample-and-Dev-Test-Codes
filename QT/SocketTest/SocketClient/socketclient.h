#ifndef SOCKETCLIENT_H
#define SOCKETCLIENT_H
#include <QTcpSocket>
#include <QMainWindow>

QT_BEGIN_NAMESPACE
namespace Ui { class SocketClient; }
QT_END_NAMESPACE

class SocketClient : public QMainWindow
{
    Q_OBJECT

public:
    SocketClient(QWidget *parent = nullptr);
    ~SocketClient();
public slots:
    void onReadyRead();
private:
    Ui::SocketClient *ui;
    QTcpSocket  _socket;
};
#endif // SOCKETCLIENT_H
