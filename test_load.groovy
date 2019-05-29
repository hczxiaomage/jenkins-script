def newScript = [];

def test1 = load './test1.groovy';
def test2 = load './test2.groovy';

echo 'p1 size is' + test1.test1Param.size;
echo 'p2 size is' + test2.test2Param.size;