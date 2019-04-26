import com.oracle.tools.packager.Log;
import org.apache.log4j.Logger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadCooperation {

    private static Account account=new Account();

    public static void main(String[] args) {
        ExecutorService executor =Executors.newFixedThreadPool(2);
        System.out.println("Thread 1\t\tThread 2\t\t\tBalance");
        executor.execute(new DepositTask());
        executor.execute(new WithdrawTask());
        executor.shutdown();

    }


    public static class DepositTask implements Runnable{

        @Override
        public void run() {
            try{
            while(true){
               account.deposit((int)(Math.random()*10)+1);
               Thread.sleep(1000);
            }
            }catch(InterruptedException ex){
            ex.printStackTrace();
            }

        }
    }

    public static class WithdrawTask implements Runnable{

        @Override
        public void run() {
            while (true){
                account.withraw((int)(Math.random())*10+1);
            }

        }
    }


    private static class Account{
        //create a new lock
        private static Lock lock=new ReentrantLock();
        private static Condition newDeposit=lock.newCondition();
        private int balance=0;
        public int getBalance(){
            return balance;
        }
        public void withraw(int amount){
            lock.lock();
            try{
                while (balance<amount){
                    System.out.println("\t\tWait for a deposit");
                    newDeposit.await();
                }
                balance-=amount;
                System.out.println("\t\t\t\tWithDraw" +amount+"\t\t\t\t"+getBalance());
            }catch (InterruptedException ex){
                ex.printStackTrace();

            }finally {
                lock.unlock();
            }
        }
            private void deposit(int amount){
            lock.lock();//Acquire the lock
            try {
                balance+=amount;
                System.out.println("Deposit"+amount+"\t\t\t"+getBalance());
                newDeposit.signalAll();
            }catch (Exception e){

            }finally {
                lock.unlock();//Release the lock
            }
        }
    }
}
