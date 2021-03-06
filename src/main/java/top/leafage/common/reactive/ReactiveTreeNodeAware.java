package top.leafage.common.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.TreeNode;
import top.leafage.common.basic.TreeNodeAware;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public interface ReactiveTreeNodeAware<T> extends TreeNodeAware<T> {

    /**
     * 处理子节点
     *
     * @param superior 上级数据
     * @param children 子节点
     * @return 树节点数据集
     */
    default Flux<TreeNode> children(T superior, Flux<T> children) {
        return this.children(superior, children, null);
    }

    /**
     * 处理子节点
     *
     * @param superior 上级数据
     * @param children 子节点
     * @param expand   扩展属性
     * @return 树节点数据集
     */
    default Flux<TreeNode> children(T superior, Flux<T> children, Set<String> expand) {
        Class<?> aClass = superior.getClass();
        try {
            Object superiorId = aClass.getSuperclass().getMethod("getId").invoke(superior);
            Object superiorName = aClass.getMethod("getName").invoke(superior);

            return children.filter(child -> this.check(superiorId, child)).flatMap(child -> {
                Class<?> childClass = child.getClass();
                try {
                    String code = childClass.getMethod("getCode").invoke(child).toString();
                    Object name = childClass.getMethod("getName").invoke(child);

                    TreeNode treeNode = new TreeNode(code, name != null ? name.toString() : null);
                    treeNode.setSuperior(superiorName != null ? superiorName.toString() : null);

                    this.children(child, children, expand).collectList().map(treeNodes -> {
                        treeNode.setChildren(treeNodes);
                        return treeNode;
                    });

                    // deal expand
                    this.expand(treeNode, childClass, child, expand);

                    return Mono.just(treeNode);
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return Mono.empty();
            });
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return Flux.empty();
    }

    /**
     * 检查是否上下级节点
     *
     * @param superiorId 上级节点ID
     * @param child      对象实例
     * @return true-是，false-否
     */
    default boolean check(Object superiorId, T child) {
        Class<?> childClass = child.getClass();
        try {
            Object superior = childClass.getMethod("getSuperior").invoke(child);
            return superiorId.equals(superior);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return false;
        }
    }
}
