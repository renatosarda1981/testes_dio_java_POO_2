import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Transacao {
    private final String tipo; // "Depósito" ou "Saque"
    private final double valor;

    public Transacao(String tipo, double valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return tipo + " de R$ " + String.format("%.2f", valor);
    }
}

public class BancoStreamAPI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Transacao> transacoes = new ArrayList<>();

        // Entrada do saldo inicial
        System.out.print("Informe o saldo inicial da conta: R$ ");
        double saldoInicial = scanner.nextDouble();

        // Entrada da quantidade de transações
        System.out.print("Quantas transações você deseja realizar? ");
        int quantidadeTransacoes = scanner.nextInt();

        // Captura das transações
        for (int i = 1; i <= quantidadeTransacoes; i++) {
            System.out.println("\nTransação " + i + ":");
            System.out.print("Digite 'D' para Depósito ou 'S' para Saque: ");
            char tipo = scanner.next().toUpperCase().charAt(0);

            System.out.print("Informe o valor da transação: R$ ");
            double valor = scanner.nextDouble();

            if (tipo == 'D') {
                transacoes.add(new Transacao("Depósito", valor));
            } else if (tipo == 'S') {
                // Verificar saldo para saque
                double saldoAtual = saldoInicial +
                        transacoes.stream()
                                .filter(t -> t.getTipo().equals("Depósito"))
                                .mapToDouble(Transacao::getValor)
                                .sum()
                        - transacoes.stream()
                        .filter(t -> t.getTipo().equals("Saque"))
                        .mapToDouble(Transacao::getValor)
                        .sum();

                if (valor > saldoAtual) {
                    System.out.println("Saldo insuficiente para realizar o saque.");
                    transacoes.add(new Transacao("Tentativa de saque (Falha)", valor));
                } else {
                    transacoes.add(new Transacao("Saque", valor));
                }
            } else {
                System.out.println("Tipo de transação inválido. Ignorada.");
                transacoes.add(new Transacao("Transação inválida", 0));
            }
        }

        // Cálculo do saldo final usando Stream API
        double saldoFinal = saldoInicial +
                transacoes.stream()
                        .filter(t -> t.getTipo().equals("Depósito"))
                        .mapToDouble(Transacao::getValor)
                        .sum()
                - transacoes.stream()
                .filter(t -> t.getTipo().equals("Saque"))
                .mapToDouble(Transacao::getValor)
                .sum();

        // Exibindo o saldo final e as transações
        System.out.println("\nSaldo final: R$ " + String.format("%.2f", saldoFinal));
        System.out.println("Histórico de transações:");
        transacoes.forEach(System.out::println);

        scanner.close();
    }
}