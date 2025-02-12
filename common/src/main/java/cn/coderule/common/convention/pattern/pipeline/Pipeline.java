package cn.coderule.common.convention.pattern.pipeline;

import lombok.Getter;
import lombok.Setter;
import cn.coderule.common.util.lang.collection.CollectionUtil;

import java.util.LinkedList;

@Getter
@Setter
public class Pipeline<T extends PipelineContext> {

    protected LinkedList<PipelineNode<T>> pipelineNodes;

    public Pipeline(LinkedList<PipelineNode<T>> pipelineNodes) {
        this.pipelineNodes = pipelineNodes;
    }

    public Pipeline() {
        initPipelineNodes();
    }

    public void addPipelineNode(PipelineNode<T> pipelineNode) {
        if (CollectionUtil.isEmpty(pipelineNodes)) {
            initPipelineNodes();
        }

        this.pipelineNodes.add(pipelineNode);
    }

    private void initPipelineNodes() {
        this.pipelineNodes = new LinkedList<>();
    }

    public void process(T context) {
        for (PipelineNode<T> pipelineNode:pipelineNodes) {
            if (!pipelineNode.shouldProceed(context)) {
                continue;
            }

            pipelineNode.process(context);
            if (context.isTerminate()) {
                break;
            }
        }
    }


}
