package net.survival.graphics.model

import net.survival.render.ModelType
import org.lwjgl.assimp.*
import java.io.File
import java.nio.IntBuffer
import java.nio.file.Paths

class StaticModel private constructor(
    val meshes: Array<Mesh>,
    val absoluteFilePath: String
) {
    class Mesh private constructor(
        val vertexCount: Int,
        val texturePath: String
    ) {
        val vertices: FloatArray = FloatArray(vertexCount * 8)

        companion object {
            fun fromAssimpMeshAndMaterial(
                aiMesh: AIMesh,
                aiMaterial: AIMaterial,
                modelFilePath: String
            ): Mesh {
                val totalVertices = getTotalIndexedVertices(aiMesh)
                val texturePath = getTexturePath(aiMaterial, modelFilePath)
                val mesh = Mesh(totalVertices, texturePath)
                importVertices(aiMesh, mesh)
                return mesh
            }

            private fun getTotalIndexedVertices(mesh: AIMesh): Int {
                var total = 0
                val faces = mesh.mFaces()
                while (faces.hasRemaining()) {
                    val face = faces.get()
                    total += face.mNumIndices()
                }
                return total
            }

            private fun getTexturePath(material: AIMaterial, modelFilePath: String): String {
                val aiTexturePath = AIString.calloc()
                Assimp.aiGetMaterialTexture(
                    material,
                    Assimp.aiTextureType_DIFFUSE,
                    0,
                    aiTexturePath,
                    null as IntBuffer?,
                    null, null, null, null, null
                )

                var texturePath = aiTexturePath.dataString()
                aiTexturePath.close()

                texturePath = Paths.get(File(modelFilePath).parent, texturePath).toString()
                return texturePath
            }

            private fun importVertices(src: AIMesh?, dest: Mesh) {
                var destIndex = 0
                val faces = src!!.mFaces()
                while (faces.hasRemaining()) {
                    val face = faces.get()
                    val indices = face.mIndices()
                    while (indices.hasRemaining()) {
                        val srcIndex = indices.get()
                        val position = src.mVertices()[srcIndex]
                        val normal = src.mNormals()!![srcIndex]
                        val texCoord = src.mTextureCoords(0)!![srcIndex]
                        dest.vertices[destIndex++] = position.x()
                        dest.vertices[destIndex++] = position.y()
                        dest.vertices[destIndex++] = position.z()
                        dest.vertices[destIndex++] = normal.x()
                        dest.vertices[destIndex++] = normal.y()
                        dest.vertices[destIndex++] = normal.z()
                        dest.vertices[destIndex++] = texCoord.x()
                        dest.vertices[destIndex++] = texCoord.y()
                    }
                }
            }
        }
    }

    companion object {
        val chicken = fromFile("./assets/models/entities/chicken.obj")
        val cow = fromFile("./assets/models/entities/cow.obj")
        val goat = fromFile("./assets/models/entities/goat.obj")
        val human = fromFile("./assets/models/entities/human.obj")
        val pig = fromFile("./assets/models/entities/pig.obj")
        val slime = fromFile("./assets/models/entities/slime.obj")

        @JvmStatic
        fun fromModelType(modelType: ModelType): StaticModel =
            when (modelType) {
                ModelType.CHICKEN -> chicken
                ModelType.COW -> cow
                ModelType.GOAT -> goat
                ModelType.HUMAN -> human
                ModelType.PIG -> pig
                ModelType.SLIME -> slime
            }

        fun fromFile(filePath: String): StaticModel {
            val file = File(filePath)
            val absoluteFilePath = file.absolutePath
            Assimp.aiImportFile(
                absoluteFilePath,
                Assimp.aiProcess_FlipUVs
                    or Assimp.aiProcess_PreTransformVertices
                    or Assimp.aiProcess_SortByPType
                    or Assimp.aiProcess_Triangulate
            ).use { scene ->
                val aiMeshes = getMeshes(scene!!)
                val aiMaterials = getMaterials(scene)

                val meshes = ArrayList<Mesh>(aiMeshes.size)
                for (i in aiMeshes.indices) {
                    val aiMesh = aiMeshes[i]
                    val aiMaterial = aiMaterials[aiMesh!!.mMaterialIndex()]
                    meshes.add(Mesh.fromAssimpMeshAndMaterial(
                        aiMesh,
                        aiMaterial!!,
                        absoluteFilePath
                    ))
                }

                return StaticModel(meshes.toTypedArray(), absoluteFilePath)
            }
        }

        private fun getMeshes(scene: AIScene): Array<AIMesh?> {
            val meshPointers = scene.mMeshes()
            val meshes = arrayOfNulls<AIMesh>(scene.mNumMeshes())
            for (i in meshes.indices) {
                meshes[i] = AIMesh.create(meshPointers!!.get())
            }
            return meshes
        }

        private fun getMaterials(scene: AIScene): Array<AIMaterial?> {
            val materialPointers = scene.mMaterials()
            val materials = arrayOfNulls<AIMaterial>(scene.mNumMaterials())
            for (i in materials.indices) {
                materials[i] = AIMaterial.create(materialPointers!!.get())
            }
            return materials
        }
    }
}