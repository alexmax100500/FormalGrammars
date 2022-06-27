import java.util.*;

public class Main {
    static Map<Character, List<String>> grammar;
    static Map<Character, Integer> map;
    static Integer[][] matrix;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();
        System.out.println(isDerivable(line+"#"));
    }

    public static boolean isDerivable(String line) {
        init();
        line += "#";
        char[] array = line.toCharArray();
        int pointer = 0;

        Stack<Character> stack = new Stack<>();
        stack.add('#');
        boolean failure = false;

        if (array.length == 1) {
            return false;
        }
        while (pointer <= array.length) {
            char c = array[pointer];
            if (!map.containsKey(c)) {
                failure = true;
                break;
            }
            if (c == '#' && stack.size() == 2 && stack.peek() == 'S')
                break;
            Integer relation = getRelation(stack.peek(), array[pointer]);
            if (relation == null) {
                failure = true;
                break;
            }
            if (relation <= 0) {
                stack.push(c);
                pointer++;
            } else {
                StringBuilder sb = new StringBuilder();
                char last;
                do {
                    last = stack.pop();
                    sb.insert(0, last);
                } while (getRelation(stack.peek(), last) >= 0);
                Iterator<Map.Entry<Character, List<String>>> iterator = grammar.entrySet().iterator();
                boolean found = false;
                char leftPart = 0;
                while (iterator.hasNext() && !found) {
                    Map.Entry<Character, List<String>> entry = iterator.next();
                    if (entry.getValue().contains(sb.toString())) {
                        found = true;
                        leftPart = entry.getKey();
                    }
                }
                if (!found) {
                    failure = true;
                    break;
                }
                stack.add(leftPart);
            }
        }
        StringBuilder sb = new StringBuilder();
        try {
            sb.insert(0, stack.pop());
            sb.insert(0, stack.pop());
        } catch (EmptyStackException e) {
            return false;
        }
        return !failure && sb.toString().equals("#S");
    }


    private static void init() {
        grammar = new HashMap<>();
        List<String> S = new ArrayList<>();
        S.add("aB");
        S.add("aA");
        grammar.put('S', S);

        List<String> A = new ArrayList<>();
        A.add("b");
        A.add("bS");
        A.add("dCA");
        grammar.put('A', A);
        
        List<String> B = new ArrayList<>();
        B.add("cS");
        B.add("c");
        B.add("dDB");
        grammar.put('B', B);

        List<String> C = new ArrayList<>();
        C.add("A");
        grammar.put('C', C);

        List<String> D = new ArrayList<>();
        D.add("B");
        grammar.put('D', D);

        map = new HashMap<>();
        map.put('S', 0);
        map.put('A', 1);
        map.put('B', 2);
        map.put('C', 3);
        map.put('D', 4);
        map.put('a', 5);
        map.put('b', 6);
        map.put('d', 7);
        map.put('c', 8);
        map.put('#', 9);

        matrix = new Integer[][]{
                {null,   1,     1,      null, null,     null,    1,     1,  1,      1},
                {null,   1,     1,      null, null,     null,    1,     1,  1,      1},
                {null,   1,     1,      null, null,     null,    1,     1,  1,      1},
                {null,   0,     null,   null, null,     null,    -1,    -1, null,   1},
                {null,   null,  0,      null, null,     null,    null,  -1, -1,     1},
                {null,   0,     0,      null, null,     null,    -1,    -1, -1,     1},
                {0,      1,     1,      null, null,     -1,      1,     1,  1,      1},
                {null,   -1,    -1,     0,    0,        null,    -1,    -1, -1,     1},
                {0,      1,     1,      null, null,     -1,      1,     1,  1,      1},
                {-1,     -1,    -1,     -1,   -1,       -1,      -1,    -1, -1,     null},
        };
    }

    private static Integer getRelation(char left, char right) {
        return matrix[map.get(left)][map.get(right)];
    }
}
