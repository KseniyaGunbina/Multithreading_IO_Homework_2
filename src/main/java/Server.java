import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
    private static String deleteBsp(String msg) {
        String[] arr = msg.split(" ");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            result.append(arr[i]);
        }
        return result.toString();
    }

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost", 23444));

        while (true){
            try (SocketChannel socketChannel = serverChannel.accept()) {
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

                while (socketChannel.isConnected()) {
                    int bytesCount = socketChannel.read(inputBuffer);
                    if (bytesCount == -1)
                        break;
                    final String clientMsg = new String(inputBuffer.array(), 0,
                            bytesCount, StandardCharsets.UTF_8);
                    inputBuffer.clear();
                    System.out.println("Получено сообщение от клиента: " + clientMsg);
                    socketChannel.write(ByteBuffer.wrap(
                            ("***** Сообщение без пробелов: " + deleteBsp(clientMsg)).getBytes(StandardCharsets.UTF_8)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
