package shop.ui;


/**
 * @param <S> Object of type1
 * @param <T> Object of type2
 */
final class GenericPair<S,T> {
    private S prompt;

    // UIFormMenu objFormMenu;
    private T testAction;

    GenericPair(S _prompt, T _testAction) {
        this.prompt = _prompt;
        this.testAction = _testAction;
    }



    public S getPrompt() {
        return prompt;
    }

    public T getTestAction() {
        return testAction;
    }




//    GenericPair(String thePrompt, UIFormMenu _objFormMenu) {
//        prompt = thePrompt;
//        objFormMenu = _objFormMenu;
//    }
}

